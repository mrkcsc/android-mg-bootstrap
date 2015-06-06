package com.miguelgaeta.bootstrap.mg_keyboard;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.util.List;

import lombok.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 3/27/15.
 *
 * TODO: Investigate using getHeight() on root view.
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

    // Is full screen.
    private boolean fullscreen;

    MGKeyboardLayoutListener(@NonNull View rootView, @NonNull Boolean fullscreen, @NonNull Observable<Void> paused) {

        // Set the root view.
        this.keyboardRootView = rootView;

        // Save paused.
        this.paused = paused;

        // Save fullscreen state.
        this.fullscreen = fullscreen;
    }

    /**
     * Use the global layout listener as a way to
     * heuristically determine the keyboard state.
     */
    @Override
    public void onGlobalLayout() {

        // Fetch metrics instance.
        MGKeyboardMetrics metrics = MGKeyboard.getMetrics();

        // Current is now previous keyboard height.
        int keyboardHeightPrevious = keyboardHeightCurrent;

        // Current root view height.
        int keyboardRootViewHeight = metrics.getCurrentRootViewHeight(keyboardRootView);

        // Calculate the current keyboard height.
        keyboardHeightCurrent = metrics.getWindowHeight() - keyboardRootViewHeight;

        // The keyboard is closed but the root view is resized, restore it - this
        // can happen if user pauses an activity while a keyboard is open.
        if (fullscreen && !MGKeyboardState.isOpened() && keyboardRootViewHeight != metrics.getWindowHeight()) {

            keyboardRootView.getLayoutParams().height = metrics.getWindowHeight();
            keyboardRootView.requestLayout();
        }

        // If the height has changed in some way.
        if (keyboardHeightPrevious != keyboardHeightCurrent) {

            if (subscription != null) {
                subscription.unsubscribe();
            }

            // Fetch stored keyboard height.
            List<Integer> keyboardHeights = metrics.getKeyboardHeights(keyboardRootView.getContext());

            // Keyboard is closed if the height is zero.
            if (MGKeyboardState.isOpened() && keyboardHeightCurrent == 0) {

                resizeRootView(MGKeyboardState.CLOSED);

            // If height reaches a known keyboard height, assume open.
            } else if (!MGKeyboardState.isOpened() && keyboardHeights.contains(keyboardHeightCurrent)) {

                resizeRootView(MGKeyboardState.OPENED);

            } else {

                subscription = MGDelay
                        .delay(MGReflection.getInteger(R.integer.animation_time_standard))
                        .takeUntil(paused)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            if (!MGKeyboardState.isOpened()) {

                                // Otherwise assume keyboard is open if no additional layouts
                                // happen within a 300 millisecond window. If triggered, add
                                // this height to list of recognized keyboard heights.
                                metrics.setKeyboardHeight(keyboardRootView.getContext(), keyboardHeightCurrent);

                                resizeRootView(MGKeyboardState.OPENED);
                            }
                        });
            }
        }
    }

    /**
     * Resize root window view of activity based
     * on the keyboard offset and animate it.
     */
    private void resizeRootView(@NonNull MGKeyboardState state) {

        // Make sure the opening gets fired.
        if (state == MGKeyboardState.OPENED && MGKeyboardState.getState() == MGKeyboardState.CLOSED) {

            MGKeyboardState._state.set(MGKeyboardState.OPENING);
        }

        MGKeyboardState._state.set(state);

        if (fullscreen && MGKeyboard.getConfig().isRootViewResize()) {

            Animation animation = new MGKeyboardAnimation(keyboardRootView, keyboardRootView.getHeight(),
                    MGKeyboard.getMetrics().getWindowHeight() - keyboardHeightCurrent);

            animation.setDuration(MGReflection.getInteger(R.integer.animation_time_standard));
            animation.setInterpolator(new AccelerateDecelerateInterpolator());

            keyboardRootView.startAnimation(animation);
        }
    }
}
