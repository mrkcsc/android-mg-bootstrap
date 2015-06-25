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
    private static MGLifecycleActivityTransitionsType defaultType = MGLifecycleActivityTransitionsType.SLIDE_POP_HORIZONTAL;

    @Setter
    private MGLifecycleActivityTransitionsType type;

    @Setter
    // Transitions be played in reverse.
    private boolean reversed = false;

    // Associated activity.
    private Activity activity;

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

            MGLifecycleActivityTransitionsType.Anim anim = MGLifecycleActivityTransitionsType.getAnimationForType(getType());

            if (reversed) {

                entering = !entering;
            }

            activity.overridePendingTransition(
                entering ? anim.getOpenIn()  : anim.getCloseIn(),
                entering ? anim.getOpenOut() : anim.getCloseOut());
        }
    }

    /**
     * Fetch the animation type which is either the
     * default type or the type set for this
     * particular activity.
     */
    private MGLifecycleActivityTransitionsType getType() {

        return type != null ? type : defaultType;
    }
}
