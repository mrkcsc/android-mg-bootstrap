package com.miguelgaeta.bootstrap.keyboard;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import lombok.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 3/27/15.
 */
class MGKeyboardLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    // Minimum height of the keyboard.
    private static final int MINIMUM_KEYBOARD_HEIGHT = 300;

    // Root view associated.
    private View keyboardRootView;
    private ViewGroup.LayoutParams keyboardRootViewLP;

    // Current keyboard height.
    private int keyboardHeightCurrent;

    // Subscription.
    private Subscription subscription;

    // Paused observable.
    private Observable<Void> paused;

    MGKeyboardLayoutListener(@NonNull View rootView, @NonNull Observable<Void> paused) {

        // Set the root view.
        this.keyboardRootView = rootView;
        this.keyboardRootViewLP = rootView.getLayoutParams();

        // Save paused.
        this.paused = paused;
    }

    @Override
    public void onGlobalLayout() {

        // Fetch metrics instance.
        MGKeyboardMetrics metrics = MGKeyboard.getMetrics();

        // Current is now previous keyboard height.
        int keyboardHeightPrevious = keyboardHeightCurrent;

        // Calculate the current keyboard height.
        keyboardHeightCurrent = metrics.getCurrentKeyboardHeight(keyboardRootView);

        // If the height has changed in some way.
        if (keyboardHeightPrevious != keyboardHeightCurrent) {

            if (subscription != null) {
                subscription.unsubscribe();
            }

            if (metrics.isKeyboardOpen() && keyboardHeightCurrent == 0) {

                resizeRootView(false);

                MGLog.e("Keyboard closed.");

            } else if (keyboardHeightCurrent > MINIMUM_KEYBOARD_HEIGHT) {

                if (keyboardHeightCurrent > metrics.getKeyboardHeight()) {

                    // Update the keyboard height.
                    metrics.setKeyboardHeight(keyboardHeightCurrent);
                }

                MGLog.e("Woop: " + keyboardHeightCurrent);

                // Keyboard is open after a delay.
                subscription = MGDelay.delay(300)
                        .takeUntil(paused)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            if (!metrics.isKeyboardOpen()) {

                                MGLog.e("Keyboard open.");

                                resizeRootView(true);
                            }
                        });
            }
        }
    }

    private void resizeRootView(boolean keyboardOpen) {

        MGKeyboard.getMetrics().setKeyboardOpen(keyboardOpen);

        Animation a = new MGKeyboard.ShowAnim(keyboardRootView, keyboardRootViewLP.height, MGKeyboard.getMetrics().getWindowHeight() - keyboardHeightCurrent);
        a.setDuration(350);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        keyboardRootView.startAnimation(a);


        // Update and resize the view if in fullscreen mode.
        //keyboardRootViewLP.height = MGKeyboard.getMetrics().getWindowHeight() - keyboardHeightCurrent;
        //keyboardRootView.requestLayout();
    }
}
