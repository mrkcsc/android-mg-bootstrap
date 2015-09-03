package com.miguelgaeta.bootstrap.mg_rx;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 9/2/15.
 */
@SuppressWarnings("UnusedDeclaration") @AllArgsConstructor(staticName = "create")
public class MGRxRetry implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;

    private int retryCount = 0;

    public MGRxRetry(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {

        return attempts.flatMap(throwable -> {
            if (++retryCount < maxRetries) {
                // When this Observable calls onNext, the original
                // Observable will be retried (i.e. re-subscribed).
                return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            }

            // Max retries hit. Just pass the error along.
            return Observable.error(throwable);
        });
    }
}
