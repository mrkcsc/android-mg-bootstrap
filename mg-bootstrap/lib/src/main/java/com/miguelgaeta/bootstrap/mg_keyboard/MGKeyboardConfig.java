package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.widget.FrameLayout;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboardConfig {

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Integer rootViewResourceId;

    public void init(Application application) {

        // Register the callbacks.
        registerLifecycleCallbacks(application);
    }

    /**
     * Fetch root view from activity or using
     * configured resource Id if present.
     */
    View getRootView(Activity activity) {

        View rootView = null;

        if (rootViewResourceId != null) {
            rootView = activity.findViewById(rootViewResourceId);
        }

        if (rootView == null) {
            rootView = ((FrameLayout)activity.findViewById(android.R.id.content)).getChildAt(0);
        }

        return rootView;
    }

    private void setGlobalLayoutListener(@NonNull final Activity activity, @NonNull Observable<Void> paused) {

        // Fetch root view.
        View rootView = getRootView(activity);

        // Create instance of keyboard layout listener.
        MGKeyboardLayoutListener listener = new MGKeyboardLayoutListener
                (rootView, MGKeyboard.getMetrics().isFullscreen(activity), paused);

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
