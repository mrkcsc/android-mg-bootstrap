package com.miguelgaeta.bootstrap.mg_rest;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.miguelgaeta.bootstrap.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import retrofit.RetrofitError;
import rx.functions.Action1;

/**
 * Created by mrkcsc on 2/10/15.
 */
@SuppressWarnings("UnusedDeclaration") @RequiredArgsConstructor
public class MGRestClientError implements Action1<Throwable> {

    private final Context context;
    private final Action1<Throwable> callback;

    public MGRestClientError(final Context context) {
        this(context, null);
    }

    @Override
    public void call(Throwable throwable) {

        if (throwable instanceof RetrofitError && context != null) {
            throwableIsRetrofitError((RetrofitError) throwable, context);
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
    private static void throwableIsRetrofitError(RetrofitError retrofitError, final Context context) {

        List<String> errorMessages = new ArrayList<>();

        boolean seriousError = false;

        switch (retrofitError.getKind()) {

            case NETWORK:

                final Throwable cause = retrofitError.getCause();

                if (cause == null) {
                    errorMessages.add(context.getString(R.string.shared_rest_network_error));
                } else if (retrofitError.getCause() instanceof ConnectException) {
                    errorMessages.add(context.getString(R.string.shared_rest_network_connection));
                } else if (retrofitError.getCause() instanceof SocketTimeoutException) {
                    errorMessages.add(context.getString(R.string.shared_rest_network_timeout));
                } else {
                    errorMessages.add(context.getString(R.string.shared_rest_network_error));
                }

                break;

            case HTTP:

                // Try to extract error message from HTTP result.
                errorMessages.addAll(tryHandleErrorResult(retrofitError, context));
                break;

            case CONVERSION:
            case UNEXPECTED:

                // Show friendly message to the user.
                errorMessages.add(context.getString(R.string.shared_rest_exception_error));

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
    private static List<String> tryHandleErrorResult(RetrofitError retrofitError, final Context context) {

        List<String> errorMessages = new ArrayList<>();

        try {

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
                    errorMessages.add(context.getString(R.string.shared_rest_unknown_error));
                }
            }

        } catch (Exception e) {

            // Last resort - emit the raw error message.
            errorMessages.add(retrofitError.getMessage());
        }

        return errorMessages;
    }

    /**
     * Use a generic approach to try to extract error
     * messages from an error result.  Assumes a JSON
     * object with error keys mapped to arrays
     * of string messages.
     */
    private static List<String> tryHandleErrorResultGeneric(RetrofitError error) {

        final List<String> errorMessages = new ArrayList<>();

        try {

            final JsonObject jsonObject = (JsonObject) error.getBodyAs(JsonObject.class);

            for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

                final String errorMessage = entry.getValue().getAsJsonArray().getAsString();
                errorMessages.add(errorMessage);
            }

        } catch (Exception ignored) { }

        return errorMessages;
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
