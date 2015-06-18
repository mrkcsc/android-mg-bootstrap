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
import lombok.RequiredArgsConstructor;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 3/27/15.
 */
@RequiredArgsConstructor(staticName = "create")
class MGKeyboardLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    @NonNull
    private View keyboardRootView;

    // Current keyboard height.
    private int keyboardHeightCurrent;

    // Subscription.
    private Subscription subscription;

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
        int keyboardRootViewHeight = MGKeyboardMetrics.getCurrentRootViewHeight(keyboardRootView);

        // Calculate the current keyboard height.
        keyboardHeightCurrent = MGKeyboardMetrics.getWindowHeight() - keyboardRootViewHeight;

        // The keyboard is closed but the root view is resized, restore it - this
        // can happen if user pauses an activity while a keyboard is open.
        if (MGKeyboardMetrics.isFullscreen() && !MGKeyboardState.isOpened() && keyboardRootViewHeight != MGKeyboardMetrics.getWindowHeight()) {

            keyboardRootView.getLayoutParams().height = MGKeyboardMetrics.getWindowHeight();
            keyboardRootView.requestLayout();
        }

        // If the height has changed in some way.
        if (keyboardHeightPrevious != keyboardHeightCurrent) {

            unsubscribe();

            // Fetch stored keyboard height.
            List<Integer> keyboardHeights = metrics.getKeyboardHeights(keyboardRootView.getContext());

            // Keyboard is closed if the height is zero.
            if (MGKeyboardState.isOpened() && keyboardHeightCurrent == 0) {

                resizeRootView(MGKeyboardState.CLOSED);

            // If height reaches a known keyboard height, assume open.
            } else if (!MGKeyboardState.isOpened() && keyboardHeights.contains(keyboardHeightCurrent)) {

                resizeRootView(MGKeyboardState.OPENED);

            } else {

                subscription = MGDelay.delay(MGKeyboardMetrics.getOpenDelay()).observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {

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

        MGKeyboardState.set(state);

        if (MGKeyboardMetrics.isFullscreen() && MGKeyboard.getConfig().isRootViewResize()) {

            Animation animation = new MGKeyboardAnimation(keyboardRootView, keyboardRootView.getHeight(),
                    MGKeyboardMetrics.getWindowHeight() - keyboardHeightCurrent);

            animation.setDuration(MGReflection.getInteger(R.integer.animation_time_standard));
            animation.setInterpolator(new AccelerateDecelerateInterpolator());

            keyboardRootView.startAnimation(animation);
        }
    }

    public void unsubscribe() {

        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
