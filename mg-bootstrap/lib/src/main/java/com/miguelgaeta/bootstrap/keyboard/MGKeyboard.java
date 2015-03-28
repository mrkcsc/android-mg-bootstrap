package com.miguelgaeta.bootstrap.keyboard;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGKeyboard {

    @Getter(lazy = true)
    private static final MGKeyboardConfig config = new MGKeyboardConfig();

    @Getter(lazy = true)
    private static final MGKeyboardMetrics metrics = new MGKeyboardMetrics();

    public static class ShowAnim extends Animation {
        int targetHeight;
        int initialHeight;
        View view;

        public ShowAnim(View view, int initialHeight, int targetHeight) {
            this.view = view;
            this.initialHeight = initialHeight;
            this.targetHeight = targetHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth,
                               int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
