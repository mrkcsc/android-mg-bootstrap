package com.miguelgaeta.bootstrap.mg_keyboard;

import android.content.Context;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/27/15.
 */
class MGKeyboardMetrics {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final MGPreference<MGKeyboardHeights> keyboardHeights = MGPreference.create("KEYBOARD_HEIGHTS");

    @Getter(AccessLevel.PACKAGE)
    private int windowHeight;

    @Getter(AccessLevel.PACKAGE) @Setter(AccessLevel.PACKAGE)
    private boolean keyboardOpen;

    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private final PublishSubject<Boolean> opened = PublishSubject.create();

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
