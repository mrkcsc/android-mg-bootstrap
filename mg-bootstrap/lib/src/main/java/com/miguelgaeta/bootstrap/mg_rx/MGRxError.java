package com.miguelgaeta.bootstrap.mg_rx;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import lombok.AllArgsConstructor;
import retrofit.RetrofitError;
import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
@SuppressWarnings("unused") @AllArgsConstructor
public class MGRxError implements Action1<Throwable> {

    private final Action1<Throwable> callback;

    private final String message;

    private final boolean debug;

    public static MGRxError create(Action1<Throwable> callback, String message, boolean debug) {

        return new MGRxError(callback, message, debug);
    }

    public static MGRxError create(Action1<Throwable> callback, String message) {

        return create(callback, message, true);
    }

    public static MGRxError create(Action1<Throwable> callback) {

        return create(callback, "[Rx Error]");
    }

    public static MGRxError create() {

        return create(null);
    }

    /**
     * If called, log the error which will output to
     * console on debug and some user defined
     * action on production.  If a user defined
     * callback exists, execute it.
     */
    @Override
    public void call(Throwable throwable) {

        if (debug) {

            String prefixMessage = "";

            if (throwable instanceof RetrofitError) {

                prefixMessage = "[Retrofit URL: " + ((RetrofitError)throwable).getUrl() + "] ";
            }

            MGLog.e(throwable, prefixMessage + message);
        }

        if (callback != null) {
            callback.call(throwable);
        }
    }
}
