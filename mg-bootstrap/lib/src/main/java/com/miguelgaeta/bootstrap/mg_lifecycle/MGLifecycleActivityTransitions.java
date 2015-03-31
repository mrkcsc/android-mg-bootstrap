package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.Getter;
import lombok.Setter;
import rx.Observable;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("ConstantConditions")
public class MGLifecycleActivityTransitions {

    @Setter
    // Type of transition.
    private MGLifecycleActivity.Type type = MGLifecycleActivity.Type.SLIDE_POP_HORIZONTAL;

    @Setter
    // Transitions be played in reverse.
    private boolean reversed = false;

    @Getter
    // Standard delay on transitions.
    private Observable<Long> delay = MGDelay.delay(MGReflection.getInteger(R.integer.animation_time_standard));

    private Activity activity;
    
    /**
     * Hold reference to current activity.
     */
    MGLifecycleActivityTransitions(Activity activity) {
        this.activity = activity;
    }

    /**
     * Runs a custom activity transition.  Decides which
     * transitioning resources to provide backed on
     * when we are going back and if the activity is entering.
     */
    void run(boolean goingBack, boolean entering) {

        if ((!goingBack && entering) || (goingBack && !entering)) {

            String animationResourcePrefix = "activity_" + type.name().toLowerCase();

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
