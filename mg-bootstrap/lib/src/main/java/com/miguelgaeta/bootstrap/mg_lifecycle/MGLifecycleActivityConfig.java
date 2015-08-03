package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLifecycleActivityConfig {

    private AppCompatActivity activity;

    /**
     * Track if this activity is being created
     * for the first time.  This can be important
     * for activities that use fragments since
     * fragments do not get destroyed when activities
     * do so you should only add them once.
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean recreated;

    /**
     * Track if the helper lifecycle method createOrResume
     * has been invoked.  This is helpful when there is code
     * you want to either run in onCreate or subsequently
     * in the onResume lifecycle methods.
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean onCreateOrResumeInvoked;

    /**
     * Pass along associated activity into
     * the configuration object.
     */
    MGLifecycleActivityConfig(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Sets the associated activity into full
     * screen mode, and hides the action bar.
     */
    public void setFullscreen() {

        setFullscreen(true);
    }

    /**
     * Sets the associated activity into full
     * screen mode and optionally hides the action bar.
     */
    public void setFullscreen(boolean actionBarDisabled) {

        if (Build.VERSION.SDK_INT < 16) {

            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {

            View decorView = activity.getWindow().getDecorView();

            // Hide the status bar.
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = activity.getSupportActionBar();

            if (actionBar != null && actionBarDisabled) {
                actionBar.hide();
            }
        }
    }

    /**
     * Clears the entire history stack.
     */
    public void setHistoryCleared() {

        // If jelly bean or above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            // Finish all activities on
            // the current stack.
            activity.finishAffinity();
        } else {
            activity.finish();
        }
    }
}
