package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_backgrounded.MGBackgrounded;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import butterknife.ButterKnife;
import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This is a generic helper activity class for
 * android.  Support convenience methods for
 * setting the content view based on a naming
 * convention and custom activity transitions.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLifecycleActivity extends ActionBarActivity {

    public enum Type {
        STANDARD,
        SLIDE_HORIZONTAL,
        SLIDE_VERTICAL,
        SLIDE_POP_VERTICAL,
        SLIDE_POP_HORIZONTAL
    }

    private static boolean goingBack;

    @Getter
    // Standard time it takes to transition activities.
    private Observable<Long> transitionDelay = MGDelay
            .delay(MGReflection.getInteger(R.integer.animation_time_standard));

    @Getter
    // Configuration object for the activity.
    private MGLifecycleActivityConfig config = new MGLifecycleActivityConfig(this);

    @Getter
    // Custom transitions object for the activity.
    private MGLifecycleActivityTransitions transitions = new MGLifecycleActivityTransitions(this);

    protected PublishSubject<Void> paused = PublishSubject.create();

    /**
     * Handles setting the content view
     * via naming conventions.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If saved state this is not the first creation.
        getConfig().setRecreated(savedInstanceState != null);

        // Attempt to set the content view.
        MGLifecycleContentView.setContentView(this);

        // Inject butter-knife views.
        ButterKnife.inject(this);
    }

    /**
     * Handles running custom transitions.
     */
    @Override
    protected void onPause() {
        super.onPause();

        paused.onNext(null);

        MGBackgrounded.getConfig().activityPaused();

        transitions.run(goingBack, false);
    }

    /**
     * Handles running custom transitions and
     * resets the going back flag.
     */
    @Override
    protected void onResume() {
        super.onResume();

        MGBackgrounded.getConfig().activityResumed();

        transitions.run(goingBack, true);

        goingBack = false;
    }

    /**
     * Track when we are transitioning back
     * vs forwards in activities.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goingBack = true;
    }

    /**
     * Intercept base context and pass it through
     * calligraphy library for custom fonts.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Adds a clear history overload to the
     * start activity call.
     */
    public void startActivity(Intent intent, boolean clearHistory) {
        super.startActivity(intent);

        if (clearHistory) {

            config.setHistoryCleared();
        }
    }

    /**
     * Start activity without any additional
     * intent options and clear history flag.
     */
    public void startActivity(Class activityClass, boolean clearHistory) {
        Intent intent = new Intent(this, activityClass);

        startActivity(intent, clearHistory);
    }

    /**
     * Start activity without any additional
     * intent options.
     */
    public void startActivity(Class activityClass) {
        startActivity(activityClass, false);
    }
}
