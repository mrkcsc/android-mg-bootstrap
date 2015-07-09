package com.miguelgaeta.bootstrap.mg_keyboard;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Miguel Gaeta on 6/5/15.
 */
@SuppressWarnings("UnusedDeclaration")
public enum  MGKeyboardState {

    OPENED, OPENING, CLOSED;

    private static final MGPreferenceRx<MGKeyboardState> _state = MGPreferenceRx.create(CLOSED);

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

        if (get() == CLOSED && state == OPENED) {

            setInternal(state);
        }

        if (state == OPENING && get() == OPENED) {

            return;
        }

        setInternal(state);
    }

    /**
     * Because this entire utility is not an exact
     * science, attempt to self correct any
     * mismatched states.
     */
    private static void setInternal(MGKeyboardState state) {

        if (state == OPENING) {

            subscription = MGDelay.delay(MGKeyboardMetrics.getOpenDelay() * 6).subscribe(r -> {

                if (get() == OPENING) {

                    setInternal(CLOSED);
                }

            }, MGRxError.create(null, "Unable to self-correct keyboard state."));
        }

        _state.set(state);
    }

    public static boolean isOpened() {

        return get().equals(OPENED);
    }
}
