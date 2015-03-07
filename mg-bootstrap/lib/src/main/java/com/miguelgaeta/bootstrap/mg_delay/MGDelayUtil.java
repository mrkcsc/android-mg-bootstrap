package com.miguelgaeta.bootstrap.mg_delay;

import java.util.concurrent.TimeUnit;

import lombok.NonNull;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 2/17/15.
 */
class MGDelayUtil {

    /**
     * Setup an observable object based
     * on the specified delay parameters.
     */
    static Observable<Long> observe(long delayMilliseconds, boolean loop, boolean startImmediately) {

        // Create timer or looping observable.
        Observable<Long> observable = loop ?
                Observable.interval(delayMilliseconds, TimeUnit.MILLISECONDS) :
                Observable.timer   (delayMilliseconds, TimeUnit.MILLISECONDS);

        if (startImmediately) {

            // Start right away.
            observable = observable.startWith(0L);
        }

        return observable;
    }

    /**
     * Setup an observable object based on the
     * specified delayed parameters and invoke
     * callback on observable subscription.
     */
    static void observe(long delayMilliseconds, boolean loop, boolean startImmediately,
                        @NonNull final MGDelayCallback callback) {

        MGDelayUtil.observe(delayMilliseconds, loop, startImmediately)
                .subscribe(o -> callback.run());
    }
}
