package com.miguelgaeta.bootstrap.keyboarder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

/**
 * Created by Miguel Gaeta on 11/19/15.
 */
public class Keyboarder {

    private final View rootView;
    private final OnGlobalLayoutListener rootViewOnGlobalLayoutListener;
    private final InputMethodManager inputMethodManager;

    private boolean destroyed = false;
    private boolean opened = false;

    public static Keyboarder create(Activity activity) {

        return new Keyboarder(activity);
    }

    private Keyboarder(final @NonNull Activity activity) {

        inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        rootViewOnGlobalLayoutListener = rootView == null ? null : new OnGlobalLayoutListener(rootView);

        if (rootViewOnGlobalLayoutListener != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(rootViewOnGlobalLayoutListener);
        }
    }

    @Synchronized
    private void setDestroyed() {

        destroyed = true;
    }

    @Synchronized
    public boolean isDestroyed() {

        return destroyed;
    }

    @Synchronized
    public void destroy() {

        if (rootViewOnGlobalLayoutListener != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(rootViewOnGlobalLayoutListener);
        }

        setDestroyed();
    }

    @Synchronized
    public boolean isOpened() {

        return opened;
    }

    @Synchronized
    private void setOpened(boolean opened) {

        this.opened = opened;
    }

    public void open() {

        if (inputMethodManager != null && rootView != null && !isDestroyed()) {
            inputMethodManager.toggleSoftInputFromWindow(rootView.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void close() {

        if (inputMethodManager != null && rootView != null && !isDestroyed()) {
            inputMethodManager.hideSoftInputFromWindow(rootView.getApplicationWindowToken(), 0);
        }
    }

    public static class Global {

        private static Global global;
        private Keyboarder keyboarder = null;

        @Synchronized
        public static Keyboarder getInstance() {

            return global.keyboarder;
        }

        @Synchronized
        public static void initialize(Application application) {

            if (global != null) {
                global = new Global(application);
            }
        }

        private Global(final @NonNull Application application) {

            application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks());
        }

        private class ActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) { }

            @Override
            public void onActivityStarted(Activity activity) { }

            @Override
            public void onActivityResumed(Activity activity) {

                keyboarder = Keyboarder.create(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

                keyboarder.destroy();
            }

            @Override
            public void onActivityStopped(Activity activity) { }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

            @Override
            public void onActivityDestroyed(Activity activity) { }
        }
    }

    @RequiredArgsConstructor
    private static class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private final View rootView;

        @Override
        public void onGlobalLayout() {

            MGLog.e("Global layout.");
        }
    }
}
