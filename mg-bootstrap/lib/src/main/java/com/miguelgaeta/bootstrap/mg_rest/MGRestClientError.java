package com.miguelgaeta.bootstrap.mg_rest;

import android.content.Context;
import android.widget.Toast;

import com.miguelgaeta.bootstrap.R;

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

    /**
     * Can't instantiate directly.
     */
    private MGRestClientError() {

    }

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

            // Crash on any unexpected exceptions.
            throw new RuntimeException(context.getResources()
                    .getString(R.string.shared_rest_unknown_error));
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

        String errorMessage = context.getResources().getString(R.string.shared_rest_unknown_error);

        switch (retrofitError.getKind()) {

            case NETWORK:

                // Generic network error message.
                errorMessage = context.getResources().getString(R.string.shared_rest_network_error);
                break;

            case CONVERSION:

                // Present a friendly serialization error message.
                errorMessage = context.getResources().getString(R.string.shared_rest_serialize_error);
                break;

            case HTTP:

                // Attempt to serialize error response from the server.
                MGRestClientErrorModel error = (MGRestClientErrorModel)retrofitError
                        .getBodyAs(MGRestClientErrorModel.class);

                if (error != null) {

                    // Use the server provided error message.
                    errorMessage = error.getError().getMessage();
                }
                break;

            case UNEXPECTED:

                // Pass along runtime exception.
                throw new RuntimeException();
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
