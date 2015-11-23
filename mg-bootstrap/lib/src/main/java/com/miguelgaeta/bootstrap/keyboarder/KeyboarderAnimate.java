package com.miguelgaeta.bootstrap.keyboarder;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class KeyboarderAnimate extends Animation {

    private int from, to;

    private final View view;

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final int initialHeight = getViewHeight();

    public KeyboarderAnimate(View view) {
        this.view = view;
    }

    public void animateHeightOffset(int offset) {

        from = getViewHeight();
        to = getInitialHeight() + offset;

        setDuration(MGReflection.getInteger(R.integer.animation_time_standard));
        setInterpolator(new AccelerateDecelerateInterpolator());

        view.startAnimation(this);
    }

    public void resetViewHeight() {

        view.clearAnimation();

        setViewHeight(getInitialHeight());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        setViewHeight(from + (int) ((to - from) * interpolatedTime));
    }

    @Override
    public boolean willChangeBounds() {

        return true;
    }

    private void setViewHeight(int height) {

        view.getLayoutParams().height = height;
        view.requestLayout();
    }

    private int getViewHeight() {

        return view.getHeight();
    }
}
