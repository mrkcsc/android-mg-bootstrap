package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by mrkcsc on 3/27/15.
 */
class MGKeyboardMetrics {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final MGPreference<MGKeyboardHeights> keyboardHeights = MGPreference.create("KEYBOARD_HEIGHTS", new TypeToken<MGKeyboardHeights>() {});

    @Getter
    private static int windowHeight;

    @Getter
    private static boolean fullscreen;

    @Getter
    private static final int openDelay = MGReflection.getInteger(R.integer.animation_time_standard);

    /**
     * Gets a list of recognized keyboard heights for
     * the current active keyboard identifier.
     */
    List<Integer> getKeyboardHeights(Context context) {

        // Fetch complete list of keyboard heights mapped to identifiers.
        MGKeyboardHeights keyboardHeights = getKeyboardHeights().get();

        if (keyboardHeights != null) {

            return keyboardHeights.get(getSoftKeyboardIdentifier(context));
        }

        return new ArrayList<>();
    }

    /**
     * Given a keyboard height, adds to to list of recognized
     * keyboard heights for the current active keyboard identifier.
     */
    void setKeyboardHeight(Context context, int keyboardHeight) {

        MGKeyboardHeights keyboardHeights = getKeyboardHeights().get();

        if (keyboardHeights == null) {
            keyboardHeights = new MGKeyboardHeights();
        }

        if (!keyboardHeights.contains(getSoftKeyboardIdentifier(context), keyboardHeight)) {
             keyboardHeights.add(getSoftKeyboardIdentifier(context), keyboardHeight);
        }

        getKeyboardHeights().set(keyboardHeights);
    }

    /**
     * Get current root view height.
     */
    public static int getCurrentRootViewHeight(View rootView) {

        Rect windowVisibleDisplayFrame = new Rect();

        // Fetch current height.
        rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);

        // Compute the height from rect holder.
        return (windowVisibleDisplayFrame.bottom - windowVisibleDisplayFrame.top);
    }

    /**
     * Is the activity in full screen mode.
     */
    public static void setActivityMetrics(Activity activity) {

        // First identify if we are in a full screen activity.
        fullscreen = (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;

        windowHeight = getWindowHeight(activity);

        if (!fullscreen) {

            // Account for status bar if not fullscreen.
            windowHeight -= getStatusBarHeight(activity);
        }
    }

    /**
     * Fetch the string identifier for the
     * current android soft keyboard.
     */
    private String getSoftKeyboardIdentifier(@NonNull Context context) {

        // Fetch soft keyboard identifier using a cool trick: http://bit.ly/18w0yTJ
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
    }

    private static int getWindowHeight(@NonNull Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = windowManager.getDefaultDisplay();

        Point windowSize = new Point();

        display.getSize(windowSize);

        return windowSize.y;
    }

    private static int getStatusBarHeight(@NonNull Context context) {

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {

            return context.getResources().getDimensionPixelSize(resourceId);
        }

        return 0;
    }
}
