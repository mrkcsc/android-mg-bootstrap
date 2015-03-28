package com.miguelgaeta.bootstrap.keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGKeyboardMetrics {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final MGPreference<Map<String, String>> keyboardHeights = MGPreference.createStringMap("KEYBOARD_HEIGHTS");

    @Getter(AccessLevel.PACKAGE)
    private int windowHeight;

    @Getter @Setter(AccessLevel.PACKAGE)
    private boolean keyboardOpen;

    public Integer getKeyboardHeight(Context context) {

        String keyboardHeightString = getKeyboardHeights().get().get(getSoftKeyboardIdentifier(context));

        if (keyboardHeightString != null) {

            return Integer.parseInt(keyboardHeightString);
        }

        return null;
    }

    void setKeyboardHeight(Context context, int keyboardHeight) {

        Map<String, String> key = getKeyboardHeights().get();

        key.put(getSoftKeyboardIdentifier(context), Integer.toString(keyboardHeight));

        getKeyboardHeights().set(key);
    }

    /**
     * Get current keyboard height by guessing it
     * using the current root view and window height.
     */
    int getCurrentKeyboardHeight(View rootView) {

        // Rect holder.
        Rect rect = new Rect();

        // Fetch current height.
        rootView.getWindowVisibleDisplayFrame(rect);

        // Compute the height from rect holder.
        int rootViewHeight = (rect.bottom - rect.top);

        // Track the window height.
        if (windowHeight < rootViewHeight) {
            windowHeight = rootViewHeight;
        }

        // Fetch current height of the keyboard.
        return windowHeight - rootViewHeight;
    }

    /**
     * Fetch the string identifier for the
     * current android soft keyboard.
     */
    private String getSoftKeyboardIdentifier(Context context) {

        // Fetch soft keyboard identifier using a cool trick: http://bit.ly/18w0yTJ
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
    }
}
