package com.miguelgaeta.bootstrap.mg_lifecycle;

import com.miguelgaeta.bootstrap.R;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
public enum MGLifecycleActivityTransitionsType {

    NONE,
    FADE,
    STANDARD,
    SLIDE_HORIZONTAL,
    SLIDE_VERTICAL,
    SLIDE_POP_VERTICAL,
    SLIDE_POP_HORIZONTAL;

    private static final Anim fade = Anim.create(
        R.anim.activity_fade_open_in,
        R.anim.activity_fade_open_out,
        R.anim.activity_fade_close_in,
        R.anim.activity_fade_close_out);

    private static final Anim standard = Anim.create(
        R.anim.activity_standard_open_in,
        R.anim.activity_standard_open_out,
        R.anim.activity_standard_close_in,
        R.anim.activity_standard_close_out);

    private static final Anim slideHorizontal = Anim.create(
        R.anim.activity_slide_horizontal_open_in,
        R.anim.activity_slide_horizontal_open_out,
        R.anim.activity_slide_horizontal_close_in,
        R.anim.activity_slide_horizontal_close_out);

    private static final Anim slideVertical = Anim.create(
        R.anim.activity_slide_vertical_open_in,
        R.anim.activity_slide_vertical_open_out,
        R.anim.activity_slide_vertical_close_in,
        R.anim.activity_slide_vertical_close_out);

    private static final Anim slidePopVertical = Anim.create(
        R.anim.activity_slide_pop_vertical_open_in,
        R.anim.activity_slide_pop_vertical_open_out,
        R.anim.activity_slide_pop_vertical_close_in,
        R.anim.activity_slide_pop_vertical_close_out);

    private static final Anim slidePopHorizontal = Anim.create(
        R.anim.activity_slide_pop_horizontal_open_in,
        R.anim.activity_slide_pop_horizontal_open_out,
        R.anim.activity_slide_pop_horizontal_close_in,
        R.anim.activity_slide_pop_horizontal_close_out);

    static Anim getAnimationForType(MGLifecycleActivityTransitionsType type) {

        switch (type) {

            case FADE:
                return fade;

            case STANDARD:
                return standard;

            case SLIDE_HORIZONTAL:
                return slideHorizontal;

            case SLIDE_VERTICAL:
                return slideVertical;

            case SLIDE_POP_VERTICAL:
                return slidePopVertical;

            case SLIDE_POP_HORIZONTAL:
                return slidePopHorizontal;
        }

        return Anim.create(0, 0, 0, 0);
    }

    @Getter @AllArgsConstructor(staticName = "create")
    static class Anim {

        private int openIn;
        private int openOut;

        private int closeIn;
        private int closeOut;
    }
}
