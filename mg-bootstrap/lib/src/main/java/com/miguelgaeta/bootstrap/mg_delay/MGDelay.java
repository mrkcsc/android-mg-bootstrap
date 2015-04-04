package com.miguelgaeta.bootstrap.mg_delay;

import rx.Observable;

/**
 * Created by Miguel Gaeta on 2/17/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGDelay {

    private MGDelay() {

    }

    /**
     * Main method that creates and configures a
     * delay with specified duration.
     */
    public static Observable<Void> delay(long delayMilliseconds) {

        return MGDelayUtil.observe(delayMilliseconds, false, false);
    }

    /**
     * Secondary delay overload that allows looping
     * after the first initial delay.
     */
    public static Observable<Void> delay(long delayMilliseconds, boolean loop) {

        return MGDelayUtil.observe(delayMilliseconds, loop, false);
    }

    /**
     * Looping overload that allows user to specify
     * that the looping starts immediately.
     */
    public static Observable<Void> delay(long delayMilliseconds, boolean loop, boolean startImmediately) {

        return MGDelayUtil.observe(delayMilliseconds, loop, startImmediately);
    }
}
