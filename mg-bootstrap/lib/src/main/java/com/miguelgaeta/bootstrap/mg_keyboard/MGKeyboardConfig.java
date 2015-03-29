package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.widget.FrameLayout;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

import lombok.NonNull;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboardConfig {

    public void init(Application application) {

        // Register the callbacks.
        registerLifecycleCallbacks(application);
    }

    private void setGlobalLayoutListener(@NonNull final Activity activity, @NonNull Observable<Void> paused) {

        // Fetch the root view of the activity.
        View rootView = ((FrameLayout)activity.findViewById(android.R.id.content)).getChildAt(0);

        // Fetch the root view layout params.
        FrameLayout.LayoutParams rootViewLayoutParams = (FrameLayout.LayoutParams) rootView.getLayoutParams();

        // Create instance of keyboard layout listener.
        MGKeyboardLayoutListener listener = new MGKeyboardLayoutListener(rootView, paused);

        // Set the listener.
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        // Until paused.
        paused.subscribe(result -> {

            // Close keyboard when activity paused.
            MGKeyboard.getMetrics().setKeyboardOpen(false);

            // Remove the listener.
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        });
    }

    private void registerLifecycleCallbacks(Application application) {

        // Register the callbacks.
        application.registerActivityLifecycleCallbacks(new MGLifecycleCallbacks() {

            protected PublishSubject<Void> paused = PublishSubject.create();

            @Override
            public void onActivityStarted(Activity activity) {
                super.onActivityStarted(activity);

                // Set layout listener.
                setGlobalLayoutListener(activity, paused);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                super.onActivityPaused(activity);

                // Now paused.
                paused.onNext(null);
            }
        });
    }
}
