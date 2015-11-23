package com.miguelgaeta.bootstrap.keyboarder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.annimon.stream.Stream;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

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

    private static final int LAYOUT_LISTENER_TAG = 5678;

    private final View rootView;
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

        final GlobalLayoutListener rootViewLayoutListener = new GlobalLayoutListener(state::onHeightChanged);

        if (rootView != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(rootViewLayoutListener);
            rootView.setTag(LAYOUT_LISTENER_TAG, rootViewLayoutListener);
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

        if (rootView != null &&
            rootView.getTag(LAYOUT_LISTENER_TAG) instanceof GlobalLayoutListener) {
            rootView.getViewTreeObserver()
                .removeOnGlobalLayoutListener((GlobalLayoutListener) rootView.getTag(LAYOUT_LISTENER_TAG));
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

        private class ActivityLifecycleCallbacks extends MGLifecycleCallbacks {

            @Override
            public void onActivityCreatedOrResumed(Activity activity, Bundle bundle) {
                super.onActivityCreatedOrResumed(activity, bundle);

                keyboarder = Keyboarder.create(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

                keyboarder.destroy();
            }
        }
    }

    public interface OnOpened {

        void onOpened(boolean opened);
    }

    public class State {

        private final List<OnOpened> onOpenedListeners = new ArrayList<>();

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

        public void addOnOpenedListener(@NonNull OnOpened onOpened) {

            onOpenedListeners.add(onOpened);
        }

        public void removeOnOpenedListener(@NonNull OnOpened onOpened) {

            onOpenedListeners.remove(onOpened);
        }

        @Synchronized
        public void setOpened(boolean opened) {

            if (this.opened != opened) {
                this.opened = opened;

                Stream.of(onOpenedListeners).forEach(listener -> listener.onOpened(opened));
            }
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

            onOpenedListeners.clear();

            unsubscribe();
        }

        private void unsubscribe() {

            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
        }
    }

    private interface OnGlobalLayoutHeightChanged {

        void onKeyboardHeight(int height);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private final Rect rootViewRect = new Rect();
        private int rootViewMaxHeight;
        private int keyboardHeight = -1;

        @NonNull
        private final OnGlobalLayoutHeightChanged onKeyboardHeight;

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