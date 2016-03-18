package com.miguelgaeta.bootstrap.keyboarder;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleCallbacks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
@SuppressWarnings("UnusedDeclaration")
public class Keyboarder {

    private final GlobalLayoutListener layoutListener;

    @Getter
    private final Keyboard keyboard;

    public Keyboarder(final @NonNull Activity activity) {

        this(activity, true);
    }

    public Keyboarder(final @NonNull Activity activity, boolean trackState) {

        final View rootView = activity
            .getWindow()
            .getDecorView()
            .findViewById(android.R.id.content);

        final InputMethodManager inputMethodManager = (InputMethodManager)activity
            .getSystemService(Context.INPUT_METHOD_SERVICE);

        keyboard = new Keyboard(rootView, inputMethodManager);

        layoutListener = trackState ? new GlobalLayoutListener(rootView, keyboard::onHeightChanged) : null;
    }

    public void destroy() {

        if (layoutListener != null) {
            layoutListener.destroy();
        }

        keyboard.destroy();
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

                keyboarder = new Keyboarder(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

                keyboarder.destroy();
            }
        }
    }

    @RequiredArgsConstructor
    public static class Keyboard {

        public interface OnOpened {

            void onOpened(boolean opened, int height);
        }

        private final List<OnOpened> onOpenedListeners = new ArrayList<>();

        private final View rootView;
        private final InputMethodManager inputMethodManager;

        private Boolean opened;

        private Subscription subscription;

        private boolean destroyed;

        private void onHeightChanged(int height) {

            if (height == 0) {

                setOpened(false, height);

            } else {

                subscription = Observable
                    .just(true)
                    .delay(250, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(opened ->
                        setOpened(opened, height));
            }
        }

        public void addOnOpenedListener(@NonNull OnOpened onOpened) {

            onOpenedListeners.add(onOpened);
        }

        public void removeOnOpenedListener(@NonNull OnOpened onOpened) {

            onOpenedListeners.remove(onOpened);
        }

        @Synchronized
        public void setOpened(boolean opened, int height) {

            if (this.opened == null || this.opened != opened) {
                this.opened = opened;

                for (OnOpened listener : onOpenedListeners) {

                    if (listener != null) {
                        listener.onOpened(opened, height);
                    }
                }
            }
        }

        @Synchronized
        public boolean isOpened() {

            return opened != null ? opened : false;
        }

        public static void open(InputMethodManager inputMethodManager, View view) {

            if (inputMethodManager != null && view != null) {
                inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            }
        }

        public static void open(Activity activity, View view) {

            open((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE), view);
        }

        public static void open(Activity activity) {

            open((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE),
                activity
                    .getWindow()
                    .getDecorView()
                    .findViewById(android.R.id.content));
        }

        public static void open(Fragment fragment) {

            open(fragment.getActivity());
        }

        public void open() {

            if (!destroyed) {

                open(inputMethodManager, rootView);
            }
        }

        public static void close(Activity activity, View view) {

            close((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE), view);
        }

        public static void close(InputMethodManager inputMethodManager, View view) {

            if (inputMethodManager != null && view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        }

        public static void close(Activity activity) {

            close((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE),
                activity
                    .getWindow()
                    .getDecorView()
                    .findViewById(android.R.id.content));
        }

        public static void close(Fragment fragment) {

            close(fragment.getActivity());
        }

        public void close() {

            if (!destroyed) {

                close(inputMethodManager, rootView);
            }
        }

        private void destroy() {

            setOpened(false, 0);

            onOpenedListeners.clear();

            unsubscribe();

            destroyed = true;
        }

        private void unsubscribe() {

            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
        }
    }

    private static class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        private interface OnKeyboardHeightChanged {

            void onKeyboardHeight(int height);
        }

        private final View rootView;
        private final Rect rootViewRect = new Rect();

        private int rootViewMaxHeight;
        private int keyboardHeight = -1;

        @NonNull
        private final OnKeyboardHeightChanged onKeyboardHeight;

        private GlobalLayoutListener(View rootView, OnKeyboardHeightChanged onKeyboardHeight) {

            this.rootView = rootView;
            this.onKeyboardHeight = onKeyboardHeight;

            if (rootView != null) {
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        }

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

        private void destroy() {

            if (rootView != null) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
}
