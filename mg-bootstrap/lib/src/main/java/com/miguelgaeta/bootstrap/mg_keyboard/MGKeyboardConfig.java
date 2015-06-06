package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.app.Application;
import android.view.View;
import android.widget.FrameLayout;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboardConfig {

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Integer rootViewResourceId;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private boolean rootViewResize;

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

    private void registerLifecycleCallbacks(Application application) {

        application.registerActivityLifecycleCallbacks(new MGLifecycleCallbacks() {

            private View rootView;

            MGKeyboardLayoutListener listener;

            @Override
            public void onActivityResumed(Activity activity) {
                super.onActivityResumed(activity);

                // Fetch root.
                rootView = getRootView(activity);

                // Assign the layout listener.
                listener = new MGKeyboardLayoutListener(rootView, MGKeyboard.getMetrics().isFullscreen(activity), getPaused());

                // Set the listener.
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                super.onActivityPaused(activity);

                // Close keyboard when activity paused.
                MGKeyboardState._state.set(MGKeyboardState.CLOSED);

                // Remove the listener.
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
            }
        });
    }
}
