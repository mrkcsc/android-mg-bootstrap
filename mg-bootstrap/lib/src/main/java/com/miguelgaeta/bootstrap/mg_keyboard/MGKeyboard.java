package com.miguelgaeta.bootstrap.mg_keyboard;

import lombok.Getter;
import rx.Observable;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboard {

    @Getter(lazy = true)
    private static final MGKeyboardConfig config = new MGKeyboardConfig();

    @Getter(lazy = true)
    private static final MGKeyboardMetrics metrics = new MGKeyboardMetrics();

    /**
     * Returns an observable that emits events when
     * the keyboard opens and closes.
     */
    public static Observable<Boolean> getKeyboardOpened() {

        return getMetrics().getOpened().asObservable();
    }

    /**
     * Is the keyboard currently open.
     */
    public static boolean isKeyboardOpen() {

        return getMetrics().isKeyboardOpen();
    }
}
