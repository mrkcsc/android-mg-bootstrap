package com.miguelgaeta.bootstrap.mg_rx;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 9/2/15.
 */
@SuppressWarnings("UnusedDeclaration") @RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "a")
public class MGRxRetry implements Func1<Observable<? extends Throwable>, Observable<?>> {

    @NonNull
    private Integer delayMillis;

    @Nullable
    private final Integer maxHalfLives, maxRetries;

    private int currentRetry = 0, currentHalfLife = 0;

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {

        return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (maxRetries == null || ++currentRetry < maxRetries) {

                    if (maxHalfLives != null && (maxHalfLives == -1 || ++currentHalfLife < maxHalfLives)) {
                        delayMillis *= 2;
                    }

                    // On onNext, original observable will be re-subscribed.
                    return Observable.timer(delayMillis, TimeUnit.MILLISECONDS);
                }

                // Max retries hit. Just pass the error along.
                return Observable.error(throwable);
            }
        });
    }

    public static MGRxRetry create(int delayMillis) {

        return new MGRxRetry(delayMillis, null, null);
    }

    public static MGRxRetry create(int delayMillis, int maxRetries) {
        return new MGRxRetry(delayMillis, null, maxRetries);
    }

    public static MGRxRetry createExponential(int delayMillis) {
        return new MGRxRetry(delayMillis, -1, null);
    }

    public static MGRxRetry createExponential(int delayMillis, int maxHalfLives) {
        return new MGRxRetry(delayMillis, maxHalfLives, null);
    }

    public static MGRxRetry createExponential(int delayMillis, int maxHalfLives, int maxRetries) {
        return new MGRxRetry(delayMillis, maxHalfLives, maxRetries);
    }
}
