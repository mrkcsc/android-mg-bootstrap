package com.miguelgaeta.bootstrap.mg_delay;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 2/17/15.
 */
class MGDelayUtil {

    /**
     * Setup an observable object based
     * on the specified delay parameters.
     */
    static Observable<Void> observe(long delayMilliseconds, boolean loop, boolean startImmediately) {

        // Create timer or looping observable.
        Observable<Long> observable = loop ?
                Observable.interval(delayMilliseconds, TimeUnit.MILLISECONDS) :
                Observable.timer   (delayMilliseconds, TimeUnit.MILLISECONDS);

        if (startImmediately) {

            // Start right away.
            observable = observable.startWith(0L);
        }

        return observable.onBackpressureDrop().map(new Func1<Long, Void>() {
            @Override
            public Void call(Long aLong) {
                return null;
            }
        });
    }
}
