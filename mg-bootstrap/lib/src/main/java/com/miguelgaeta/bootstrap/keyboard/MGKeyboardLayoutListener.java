package com.miguelgaeta.bootstrap.keyboard;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 3/27/15.
 */
class MGKeyboardLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    // Root view associated.
    private View keyboardRootView;

    // Current keyboard height.
    private int keyboardHeightCurrent;

    // Subscription.
    private Subscription subscription;

    // Paused observable.
    private Observable<Void> paused;

    MGKeyboardLayoutListener(@NonNull View rootView, @NonNull Observable<Void> paused) {

        // Set the root view.
        this.keyboardRootView = rootView;

        // Save paused.
        this.paused = paused;
    }

    @Override
    public void onGlobalLayout() {

        MGLog.e("Test: " + keyboardRootView.getHeight());

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

            // Fetch stored keyboard height.
            Integer keyboardHeight = metrics.getKeyboardHeight(keyboardRootView.getContext());

            MGLog.e("K: " + keyboardHeight + " kc: " + keyboardHeightCurrent);

            if (metrics.isKeyboardOpen() && keyboardHeightCurrent == 0) {

                resizeRootView(false);

            } else if (keyboardHeight == null) {

                // Keyboard is open after a delay.
                subscription = MGDelay
                        .delay(300)
                        .takeUntil(paused)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            if (!metrics.isKeyboardOpen()) {

                                MGLog.e("Keyboard open.");

                                // Update the keyboard height.
                                metrics.setKeyboardHeight(keyboardRootView.getContext(), keyboardHeightCurrent);

                                resizeRootView(true);
                            }
                        });

            } else if (!metrics.isKeyboardOpen() && keyboardHeight == keyboardHeightCurrent) {

                MGLog.e("Keyboard open instant.");

                resizeRootView(true);
            }
        }
    }

    /**
     * Resize root window view of activity based
     * on the keyboard offset and animate it.
     */
    private void resizeRootView(boolean keyboardOpen) {

        MGKeyboard.getMetrics().setKeyboardOpen(keyboardOpen);

        Animation animation = new MGKeyboardAnimation(keyboardRootView, keyboardRootView.getHeight(),
                MGKeyboard.getMetrics().getWindowHeight() - keyboardHeightCurrent);

        animation.setDuration(MGReflection.getInteger(R.integer.animation_time_standard));
        animation.setInterpolator(new AccelerateDecelerateInterpolator());

        keyboardRootView.startAnimation(animation);
    }
}
