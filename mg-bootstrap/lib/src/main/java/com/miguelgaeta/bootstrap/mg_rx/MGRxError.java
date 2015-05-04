package com.miguelgaeta.bootstrap.mg_rx;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
@SuppressWarnings("unused")
public class MGRxError implements Action1<Throwable> {

    // Override debug flag.
    private final boolean debug;

    // Run additional code.
    private final Action1<Throwable> callback;

    /**
     * Creates an instance of the RxError class which automatically handles
     * catching and logging errors that can happen in RxJava subscriptions.
     */
    private MGRxError(Action1<Throwable> callback, boolean debug) {

        this.debug = debug;
        this.callback = callback;
    }

    public static MGRxError create(Action1<Throwable> callback, boolean debug) {

        return new MGRxError(callback, debug);
    }

    public static MGRxError create(Action1<Throwable> callback) {

        return new MGRxError(callback, true);
    }

    public static MGRxError create() {

        return new MGRxError(null, true);
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

            MGLog.e(throwable, "RxError");
        }

        if (callback != null) {
            callback.call(throwable);
        }
    }
}
