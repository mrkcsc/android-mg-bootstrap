package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.Setter;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
public class MGLifecycleActivityTransitions {

    private Activity activity;

    @Setter
    private MGLifecycleActivity.Type type = MGLifecycleActivity.Type.SLIDE_POP_HORIZONTAL;

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
