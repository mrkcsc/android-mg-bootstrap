package com.miguelgaeta.bootstrap.mg_backgrounded;

import android.app.Activity;
import android.app.Application;

import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.Getter;
import rx.Subscription;

/**
 * Created by Miguel Gaeta on 5/1/15.
 */
class MGBackgroundedUtil {

    @Getter
    private Subscription backgroundedSubscription;

    @Getter(lazy = true)
    private final MGPreferenceRx<Boolean> backgrounded = MGPreferenceRx.create(null, new TypeToken<Boolean>() {}, true);

    /**
     * Time to be backgrounded.  Assume it wont
     * take more than a second to switch
     * between activities.
     */
    private static final int MILLISECONDS_UNTIL_BACKGROUNDED = 1000;

    /**
     * Background state is tracked globally across activity
     * pause and resumes.  When a pause is detected, we wait
     * a small amount of time, then push to the
     * backgrounded stream.
     *
     * If a resume is detected before the event can be emitted
     * we cancel the delay/callback and stay as not
     * backgrounded.
     *
     * For consumers, only distinct values are emitted so
     * multiple resume events do not emit multiple
     * to foreground events.
     */
    public void registerCallbacks(Application application) {

        application.registerActivityLifecycleCallbacks(new MGLifecycleCallbacks() {

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);

                if (getBackgroundedSubscription() != null) {
                    getBackgroundedSubscription().unsubscribe();
                }

                getBackgrounded().set(false);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                super.onActivityPaused(activity);

                // On a delay, trigger backgrounded.
                backgroundedSubscription = MGDelay.delay(MILLISECONDS_UNTIL_BACKGROUNDED)
                    .subscribe(timestamp -> getBackgrounded().set(true), MGRxError.create());
            }
        });
    }
}
