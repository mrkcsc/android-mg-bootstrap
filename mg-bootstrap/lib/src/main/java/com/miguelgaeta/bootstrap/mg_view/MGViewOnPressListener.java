package com.miguelgaeta.bootstrap.mg_view;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Miguel Gaeta on 5/6/15.
 */
public abstract class MGViewOnPressListener implements View.OnTouchListener {

    private boolean pressed;

    public abstract void onPress(boolean pressed);

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                if (!pressed) {
                    pressed = true;

                    onPress(true);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:

                if (pressed) {
                    pressed = false;

                    onPress(false);
                }

                break;
        }

        return false;
    }
}
