package com.miguelgaeta.bootstrap.mg_keyboard;

import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import rx.Observable;

/**
 * Created by Miguel Gaeta on 6/5/15.
 */
@SuppressWarnings("UnusedDeclaration")
public enum  MGKeyboardState {

    OPENED, OPENING, CLOSED;

    static final MGPreferenceRx<MGKeyboardState> _state = MGPreferenceRx.create(null, CLOSED);

    public static Observable<MGKeyboardState> getStateObservable() {

        return _state.get().distinctUntilChanged();
    }

    public static MGKeyboardState getState() {

        return _state.getBlocking();
    }

    public static boolean isOpened() {

        return getState().equals(OPENED);
    }
}