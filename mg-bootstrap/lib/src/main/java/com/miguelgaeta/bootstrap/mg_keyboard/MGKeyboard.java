package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboard {

    @Getter(lazy = true)
    private static final MGKeyboardConfig config = new MGKeyboardConfig();

    @Getter(lazy = true)
    private static final MGKeyboardMetrics metrics = new MGKeyboardMetrics();

    /**
     * Allows for programmatic opening and
     * closing of the soft keyboard.
     */
    public static void setKeyboardOpen(Activity activity, boolean keyboardOpen) {

        // Fetch the input method manager.
        final InputMethodManager inputMethodManager = (InputMethodManager)activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        final View rootView = MGKeyboardLifecycle.getRootView(activity);

        if (rootView == null) {
            return;
        }

        final IBinder windowToken = rootView.getApplicationWindowToken();

        if (keyboardOpen) {

            // Show the keyboard.
            inputMethodManager.toggleSoftInputFromWindow(windowToken, InputMethodManager.SHOW_FORCED, 0);
        } else {

            activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            // Hide the keyboard.
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
