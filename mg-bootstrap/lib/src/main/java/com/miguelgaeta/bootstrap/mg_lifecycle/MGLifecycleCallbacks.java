package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import lombok.Getter;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    @Getter
    private PublishSubject<Void> paused = PublishSubject.create();

    private boolean onActivityCreatedOrResumedInvoked;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

        onActivityCreatedOrResumed(activity, bundle);
        onActivityCreatedOrResumedInvoked = true;
    }

    public void onActivityCreatedOrResumed(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) { }

    @Override
    public void onActivityResumed(Activity activity) {

        if (onActivityCreatedOrResumedInvoked) {
            onActivityCreatedOrResumedInvoked = false;
        } else {

            onActivityCreatedOrResumed(activity, null);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        // Now paused.
        paused.onNext(null);
    }

    @Override
    public void onActivityStopped(Activity activity) { }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) { }

    @Override
    public void onActivityDestroyed(Activity activity) { }
}
