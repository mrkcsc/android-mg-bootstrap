package com.miguelgaeta.bootstrap.mg_keyboard;

import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import rx.Observable;

/**
 * Created by Miguel Gaeta on 6/5/15.
 */
@SuppressWarnings("UnusedDeclaration")
public enum  MGKeyboardState {

    OPENED, OPENING, CLOSED;

    private static final MGPreferenceRx<MGKeyboardState> _state = MGPreferenceRx.create(null, CLOSED);

    public static Observable<MGKeyboardState> getObservable() {

        return _state.get().distinctUntilChanged();
    }

    public static MGKeyboardState get() {

        return _state.getBlocking();
    }

    static void set(MGKeyboardState state) {

        if (get() == CLOSED && state == OPENED) {

            _state.set(OPENING);
        }

        if (state == OPENING && get() == OPENED) {

            return;
        }

        _state.set(state);
    }

    public static boolean isOpened() {

        return get().equals(OPENED);
    }
}
