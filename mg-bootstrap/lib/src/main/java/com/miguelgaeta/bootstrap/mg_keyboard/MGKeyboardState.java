package com.miguelgaeta.bootstrap.mg_keyboard;

import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Miguel Gaeta on 6/5/15.
 */
@SuppressWarnings("UnusedDeclaration")
public enum  MGKeyboardState {

    OPENED, CLOSED;

    private static final MGPreferenceRx<MGKeyboardState> _state = MGPreferenceRx.create(null, CLOSED);

    private static Subscription subscription;

    public static Observable<MGKeyboardState> getObservable() {

        return _state.get().distinctUntilChanged();
    }

    public static MGKeyboardState get() {

        return _state.get().toBlocking().mostRecent(CLOSED).iterator().next();
    }

    static void set(MGKeyboardState state) {

        if (subscription != null) {
            subscription.unsubscribe();
        }

        _state.set(state);
    }

    public static boolean isOpened() {

        return get().equals(OPENED);
    }
}
