package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    @RequiredArgsConstructor @SuppressWarnings({"UnusedDeclaration", "unchecked"})
    public static class Single<T extends Activity> extends MGLifecycleCallbacks {

        private final Class<T> clazz;

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityCreated((T) activity, bundle);
            }
        }

        @Override
        public void onActivityCreatedOrResumed(Activity activity, Bundle bundle) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityCreatedOrResumed((T) activity, bundle);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityStarted((T) activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityResumed((T) activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityPaused((T) activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityStopped((T) activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivitySaveInstanceState((T) activity, bundle);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

            if (clazz.isAssignableFrom(activity.getClass())) {

                onSingleActivityDestroyed((T) activity);
            }
        }

        public void onSingleActivityCreated(T activity, Bundle bundle) {
            super.onActivityCreated(activity, bundle);
        }

        public void onSingleActivityCreatedOrResumed(T activity, Bundle bundle) {
            super.onActivityCreatedOrResumed(activity, bundle);
        }

        public void onSingleActivityStarted(T activity) {
            super.onActivityStarted(activity);
        }

        public void onSingleActivityResumed(T activity) {
            super.onActivityResumed(activity);
        }

        public void onSingleActivityPaused(T activity) {
            super.onActivityPaused(activity);
        }

        public void onSingleActivityStopped(T activity) {
            super.onActivityStopped(activity);
        }

        public void onSingleActivitySaveInstanceState(T activity, Bundle bundle) {
            super.onActivitySaveInstanceState(activity, bundle);
        }

        public void onSingleActivityDestroyed(T activity) {
            super.onActivityDestroyed(activity);
        }
    }
}
