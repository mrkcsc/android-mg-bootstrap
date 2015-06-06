package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

/**
 * Created by Miguel Gaeta on 6/5/15.
 */
class MGKeyboardLifecycle extends MGLifecycleCallbacks {

    private View rootView;

    private MGKeyboardLayoutListener listener;

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);

        // Fetch root.
        rootView = getRootView(activity);

        // Assign the layout listener.
        listener = new MGKeyboardLayoutListener(rootView, MGKeyboard.getMetrics().isFullscreen(activity), getPaused());

        // Set the listener.
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        initializeEditTexts((ViewGroup) rootView);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        super.onActivityPaused(activity);

        // Close keyboard when activity paused.
        MGKeyboardState.set(MGKeyboardState.CLOSED);

        // Remove the listener.
        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    private void initializeEditTexts(ViewGroup viewGroup) {

        for (int i = 0; i <= viewGroup.getChildCount() - 1; i++) {

            View view = viewGroup.getChildAt(i);

            if (view instanceof ViewGroup) {

                initializeEditTexts((ViewGroup) view);
            }

            if (view instanceof EditText) {

                EditText editText = (EditText) view;

                editText.setOnFocusChangeListener((r1, r2) -> MGKeyboardState.set(MGKeyboardState.OPENING));

                editText.setOnClickListener(r1 -> MGKeyboardState.set(MGKeyboardState.OPENING));
            }
        }
    }

    /**
     * Fetch root view from activity or using configured resource id if present.
     */
    public static View getRootView(Activity activity) {

        View rootView = null;

        if (MGKeyboard.getConfig().getRootViewResourceId() != null) {
            rootView = activity.findViewById(MGKeyboard.getConfig().getRootViewResourceId());
        }

        if (rootView == null) {
            rootView = ((FrameLayout)activity.findViewById(android.R.id.content)).getChildAt(0);
        }

        return rootView;
    }
}
