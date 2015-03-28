package com.miguelgaeta.bootstrap.keyboard;

import android.graphics.Rect;
import android.view.View;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGKeyboardMetrics {

    @Getter(AccessLevel.PACKAGE)
    private int windowHeight;

    @Getter @Setter(AccessLevel.PACKAGE)
    private int keyboardHeight;

    @Getter @Setter(AccessLevel.PACKAGE)
    private boolean keyboardOpen;

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
}
