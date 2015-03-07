package com.miguelgaeta.bootstrap.mg_backgrounded;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;

import lombok.AccessLevel;
import lombok.Getter;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 2/17/15.
 */
public class MGBackgroundedConfig {

    // Tracks the backgrounded state internally.
    @Getter(value = AccessLevel.PACKAGE, lazy = true)
    private static final PublishSubject<Boolean> backgrounded = PublishSubject.create();

    /**
     * Clears existing observable subscription and
     * emits a backgrounded true flag on a delay.
     */
    @SuppressWarnings("unused")
    public void activityPaused() {

        activityResumed();

        MGDelay.delay(1000)
                .takeUntil(getBackgrounded())
                .subscribe(timestamp -> getBackgrounded().onNext(true));
    }

    /**
     * Emits a no longer backgrounded flag
     * when an activity app controls is resumed.
     */
    @SuppressWarnings("unused")
    public void activityResumed() {

        getBackgrounded().onNext(false);
    }
}