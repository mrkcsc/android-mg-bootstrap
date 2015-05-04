package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.Setter;
import rx.Observable;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration,ConstantConditions")
public class MGLifecycleActivityTransitions {

    // To ensure the most fluid transitions or animations when
    // people wait on activity transitions to finish add a small
    // buffer to the actual animation time.
    private static final int TRANSITION_DELAY_BUFFER = 100;

    @Setter
    private static Type defaultType = Type.SLIDE_POP_HORIZONTAL;

    @Setter
    private Type type;

    @Setter
    // Transitions be played in reverse.
    private boolean reversed = false;

    // Associated activity.
    private Activity activity;

    /**
     * Supported transition types.
     */
    public enum Type {
        NONE,
        FADE,
        STANDARD,
        SLIDE_HORIZONTAL,
        SLIDE_VERTICAL,
        SLIDE_POP_VERTICAL,
        SLIDE_POP_HORIZONTAL
    }

    /**
     * Hold reference to current activity.
     */
    MGLifecycleActivityTransitions(Activity activity) {
        this.activity = activity;
    }

    /**
     * Standard delay on transitions.
     */
    public Observable<Void> getDelay() {

        return  MGDelay.delay(MGReflection.getInteger(R.integer.animation_time_standard) + TRANSITION_DELAY_BUFFER);
    }

    /**
     * Runs a custom activity transition.  Decides which
     * transitioning resources to provide backed on
     * when we are going back and if the activity is entering.
     */
    void run(boolean goingBack, boolean entering) {

        if ((!goingBack && entering) || (goingBack && !entering)) {

            if (getType() == Type.NONE) {

                // Don't animate anything.
                activity.overridePendingTransition(0, 0);

            } else {

                String animationResourcePrefix = "activity_" + getType().name().toLowerCase();

                if (reversed) {

                    entering = !entering;
                }

                int enterAnimation = entering ?
                        MGReflection.getResourceId(animationResourcePrefix + "_open_in", R.anim.class) :
                        MGReflection.getResourceId(animationResourcePrefix + "_close_in", R.anim.class);

                int exitAnimation = entering ?
                        MGReflection.getResourceId(animationResourcePrefix + "_open_out", R.anim.class) :
                        MGReflection.getResourceId(animationResourcePrefix + "_close_out", R.anim.class);

                // Override the default animation activity animations.
                activity.overridePendingTransition(enterAnimation, exitAnimation);
            }
        }
    }

    /**
     * Fetch the animation type which is either the
     * default type or the type set for this
     * particular activity.
     */
    private Type getType() {

        return type != null ? type : defaultType;
    }
}
