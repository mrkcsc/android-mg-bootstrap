package com.miguelgaeta.bootstrap.keyboarder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Miguel Gaeta on 11/19/15.
 */
public class Keyboarder {

    private final View rootView;
    private final OnGlobalLayoutListener rootViewOnGlobalLayoutListener;
    private final InputMethodManager inputMethodManager;

    private boolean destroyed = false;

    @Getter
    private final State state = new State();

    public static Keyboarder create(Activity activity) {

        return new Keyboarder(activity);
    }

    private Keyboarder(final @NonNull Activity activity) {

        inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

        if (rootView != null) {
            rootViewOnGlobalLayoutListener = new OnGlobalLayoutListener(rootView, state::onHeightChanged);
        } else {
            rootViewOnGlobalLayoutListener = null;
        }

        if (rootViewOnGlobalLayoutListener != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(rootViewOnGlobalLayoutListener);
        }
    }

    @Synchronized
    private void setDestroyed() {

        state.destroy();

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

    public static class Global {

        private static Global global;
        private Keyboarder keyboarder = null;

        @Synchronized
        public static Keyboarder getInstance() {

            return global.keyboarder;
        }

        @Synchronized
        public static void initialize(Application application) {

            if (global == null) {
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

    public interface OnStateChanged {

        void onStateChanged(boolean opened, int height);
    }

    public class State {

        private final List<OnStateChanged> onStateChangedListeners = new ArrayList<>();

        private boolean opened;

        private Subscription subscription;

        private void onHeightChanged(int height) {

            if (height == 0) {

                setOpened(false);

            } else {

                subscription = Observable
                    .just(true)
                    .delay(250, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setOpened);
            }
        }

        public void addOnStateChangedListener() {

        }

        public void removeOnStateChangedListener() {

        }

        @Synchronized
        public void setOpened(boolean opened) {

            this.opened = opened;
        }

        @Synchronized
        public boolean isOpened() {

            return opened;
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

        private void destroy() {

            setOpened(false);

            unsubscribe();
        }

        private void unsubscribe() {

            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private interface OnKeyboardHeight {

            void onKeyboardHeight(int height);
        }

        private final View rootView;
        private final Rect rootViewRect = new Rect();

        private int rootViewMaxHeight;

        private int keyboardHeight = -1;

        @NonNull
        private final OnKeyboardHeight onKeyboardHeight;

        @Override
        public void onGlobalLayout() {

            final int keyboardRootViewHeight = getRootViewHeight();

            if (rootViewMaxHeight < keyboardRootViewHeight) {
                rootViewMaxHeight = keyboardRootViewHeight;
            }

            final int keyboardHeight = rootViewMaxHeight - keyboardRootViewHeight;

            if (this.keyboardHeight != keyboardHeight) {
                this.keyboardHeight = keyboardHeight;

                onKeyboardHeight.onKeyboardHeight(keyboardHeight);
            }
        }

        private int getRootViewHeight() {

            rootView.getWindowVisibleDisplayFrame(rootViewRect);

            return rootViewRect.bottom - rootViewRect.top;
        }
    }
}
