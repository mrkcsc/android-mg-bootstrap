package com.miguelgaeta.bootstrap.mg_backgrounded;

import android.app.Activity;
import android.app.Application;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Subscription;

/**
 * Created by mrkcsc on 2/17/15.
 */
public class MGBackgroundedConfig {

    // Time to be backgrounded.
    private static final int MILLISECONDS_UNTIL_BACKGROUNDED = 1000;

    /**
     * Keep track of backgrounded state, assume we are backgrounded
     * as the initial value and proceed from there by observing
     * activity lifecycle callbacks.
     */
    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private static final MGPreferenceRx<Boolean> backgrounded = MGPreferenceRx.create(null, true);

    @Getter
    private static Subscription backgroundedSubscription;

    /**
     * Initializes background tracking.
     */
    public void init(Application application) {

        application.registerActivityLifecycleCallbacks(new MGLifecycleCallbacks() {

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);

                // Cancel pending backgrounded emission.
                if (getBackgroundedSubscription() != null) {
                    getBackgroundedSubscription().unsubscribe();
                }

                // No longer backgrounded.
                MGBackgroundedConfig.getBackgrounded().set(false);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                super.onActivityPaused(activity);

                // On a delay, trigger backgrounded.
                backgroundedSubscription = MGDelay.delay(MILLISECONDS_UNTIL_BACKGROUNDED)
                    .subscribe(timestamp -> getBackgrounded().set(true));
            }
        });
    }
}