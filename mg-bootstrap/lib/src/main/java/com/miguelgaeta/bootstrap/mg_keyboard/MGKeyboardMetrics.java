package com.miguelgaeta.bootstrap.mg_keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGKeyboardMetrics {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final MGPreference<KeyboardHeights> keyboardHeights = MGPreference.create("KEYBOARD_HEIGHTS");

    @Getter(AccessLevel.PACKAGE)
    private int windowHeight;

    @Getter @Setter(AccessLevel.PACKAGE)
    private boolean keyboardOpen;

    /**
     * Gets a list of recognized keyboard heights for
     * the current active keyboard identifier.
     */
    List<Integer> getKeyboardHeights(Context context) {

        // Fetch complete list of keyboard heights mapped to identifiers.
        KeyboardHeights keyboardHeights = getKeyboardHeights().get();

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

        KeyboardHeights keyboardHeights = getKeyboardHeights().get();

        if (keyboardHeights == null) {
            keyboardHeights = new KeyboardHeights();
        }

        if (!keyboardHeights.contains(getSoftKeyboardIdentifier(context), keyboardHeight)) {
             keyboardHeights.add(getSoftKeyboardIdentifier(context), keyboardHeight);
        }

        getKeyboardHeights().set(keyboardHeights);
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

    /**
     * Helper class for storing and persisting
     * detected android keyboard heights.
     */
    private static class KeyboardHeights {

        // The actual data structure holding the keyboard heights.
        private final Map<String, List<Integer>> keyboardHeights = new HashMap<>();

        /**
         * Given a soft keyboard identifier, fetch
         * recognized associated keyboard heights.
         */
        private List<Integer> get(String softKeyboardIdentifier) {

            return keyboardHeights.get(softKeyboardIdentifier) != null ?
                   keyboardHeights.get(softKeyboardIdentifier) : new ArrayList<>();
        }

        /**
         * Given a soft keyboard identifier, is a given
         * height present in the associated keyboard heights.
         */
        private boolean contains(String softKeyboardIdentifier, int keyboardHeight) {

            return keyboardHeights.get(softKeyboardIdentifier) != null &&
                   keyboardHeights.get(softKeyboardIdentifier).contains(keyboardHeight);
        }

        /**
         * Add a new keyboard height to an associated
         * soft keyboard identifier.
         */
        private void add(String softKeyboardIdentifier, int keyboardHeight) {

            List<Integer> heights = keyboardHeights.get(softKeyboardIdentifier);

            if (heights == null) {
                heights = new ArrayList<>();
            }

            if (!heights.contains(keyboardHeight)) {
                 heights.add(keyboardHeight);
            }

            keyboardHeights.put(softKeyboardIdentifier, heights);
        }
    }
}
