package com.miguelgaeta.bootstrap.mg_delay;

import lombok.NonNull;
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
    public static Observable<Long> delay(long delayMilliseconds) {

        return MGDelayUtil.observe(delayMilliseconds, false, false);
    }

    /**
     * Secondary delay overload that allows looping
     * after the first initial delay.
     */
    public static Observable<Long> delay(long delayMilliseconds, boolean loop) {

        return MGDelayUtil.observe(delayMilliseconds, loop, false);
    }

    /**
     * Looping overload that allows user to specify
     * that the looping starts immediately.
     */
    public static Observable<Long> delay(long delayMilliseconds, boolean loop, boolean startImmediately) {

        return MGDelayUtil.observe(delayMilliseconds, loop, startImmediately);
    }

    /**
     * Main delay method with a standard callback class.
     */
    public static void delay(long delayMilliseconds,
                             @NonNull MGDelayCallback callback) {

        MGDelayUtil.observe(delayMilliseconds, false, false, callback);
    }

    /**
     * Looping override with a callback class.
     */
    public static void delay(long delayMilliseconds, boolean loop,
                             @NonNull MGDelayCallback callback) {

        MGDelayUtil.observe(delayMilliseconds, loop, false, callback);
    }

    /**
     * Looking override with a callback class,  allows user
     * to specify if it should start immediately.
     */
    public static void delay(long delayMilliseconds, boolean loop, boolean startImmediately,
                             @NonNull MGDelayCallback callback) {

        MGDelayUtil.observe(delayMilliseconds, loop, startImmediately, callback);
    }
}
