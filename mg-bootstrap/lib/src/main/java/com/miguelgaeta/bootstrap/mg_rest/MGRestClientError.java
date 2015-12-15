package com.miguelgaeta.bootstrap.mg_rest;

import android.content.Context;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import rx.functions.Action1;

/**
 * Created by mrkcsc on 2/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGRestClientError implements Action1<Throwable> {

    private Context context;

    // Allows the user to funnel an additional
    // callback to be performed after the generic
    // error handling (optional).
    private Action1<Throwable> callback;

    private MGRestClientError() { }

    /**
     * Create a standard error object.
     */
    public static MGRestClientError create(Context context) {

        return create(context, null);
    }

    /**
     * Create a standard error object with a callback.
     */
    public static MGRestClientError create(Context context, Action1<Throwable> callback) {

        MGRestClientError error = new MGRestClientError();

        error.context = context;
        error.callback = callback;

        return error;
    }

    /**
     * Generic handler for any and all retrofit
     * related errors.  Used in the RxJava API pattern
     * for retrofit.
     */
    @Override
    public void call(Throwable throwable) {

        try {

            // Attempt to handle the (possible) retrofit error.
            tryHandleRetrofitError((RetrofitError) throwable);

        } catch (Exception e) {

            // Call standard error class.
            MGRxError.create().call(throwable);
        }

        if (callback != null) {
            callback.call(throwable);
        }
    }

    /**
     * Attempt to generically handle a retrofit
     * error object.  Saves developers time by not having
     * to always explicitly check for errors
     * and keeps things consistent.
     */
    private void tryHandleRetrofitError(RetrofitError retrofitError) {

        List<String> errorMessages = new ArrayList<>();

        boolean seriousError = false;

        switch (retrofitError.getKind()) {

            case NETWORK:

                // Generic network error message.
                errorMessages.add(MGReflection.getString(R.string.shared_rest_network_error));
                break;

            case HTTP:

                // Try to extract error message from HTTP result.
                errorMessages.addAll(tryHandleErrorResult(retrofitError));
                break;

            case CONVERSION:
            case UNEXPECTED:

                // Show friendly message to the user.
                errorMessages.add(MGReflection.getString(R.string.shared_rest_unknown_error));

                // Serious error.
                seriousError = true;
        }

        if (context != null) {

            for (String errorMessage : errorMessages) {

                // Print out any attached string messages.
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }

        if (seriousError) {

            // Pass along runtime exception.
            throw new RuntimeException();
        }
    }

    /**
     * Try to handle HTTP error result from server.
     */
    private List<String> tryHandleErrorResult(RetrofitError retrofitError) {

        List<String> errorMessages = new ArrayList<>();

        try {

            retrofitError.getBody();

        } catch (Exception e) {

            // Last resort - emit the raw error message.
            errorMessages.add(retrofitError.getMessage());
        }

        if (errorMessages.size() == 0) {

            ResponseBody error = null;

            try {

                // Attempt to serialize error response from the server.
                error = (ResponseBody)retrofitError.getBodyAs(ResponseBody.class);

            } catch (Exception ignored) { }

            if (error != null &&
                error.getMessage() != null) {

                errorMessages.add(error.getMessage());

            } else {

                errorMessages.addAll(tryHandleErrorResultGeneric(retrofitError));

                if (errorMessages.isEmpty()) {
                    errorMessages.add(MGReflection.getString(R.string.shared_rest_unknown_error));
                }
            }
        }

        return errorMessages;
    }

    /**
     * Use a generic approach to try to extract error
     * messages from an error result.  Assumes a JSON
     * object with error keys mapped to arrays
     * of string messages.
     */
    private List<String> tryHandleErrorResultGeneric(RetrofitError error) {

        try {

            return
                Stream
                    .of((JsonObject)error.getBodyAs(JsonObject.class))
                    .map(JsonObject::entrySet)
                    .flatMap(Stream::of)
                    .map(Map.Entry::getValue)
                    .map(JsonElement::getAsJsonArray)
                    .map(JsonArray::getAsString)
                    .collect(Collectors.toList());

        } catch (Exception ignored) {

            return Collections.emptyList();
        }
    }

    private static class ResponseBody {

        private Details error;

        private String message;

        private static class Details {

            private String message;
        }

        public String getMessage() {

            return error != null ? error.message : message;
        }
    }
}
