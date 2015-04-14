package com.miguelgaeta.bootstrap.mg_backgrounded;

import lombok.Getter;
import rx.Observable;

/**
 * Created by mrkcsc on 12/3/14.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGBackgrounded {

    @Getter
    private static MGBackgroundedConfig config = new MGBackgroundedConfig();

    /**
     * Is the application currently in the background.  "In the background"
     * is defined as when we have left the foreground for more than one second.
     */
    public static boolean isBackgrounded() {

        // Fetch the most recent backgrounded value.
        return MGBackgroundedConfig.getBackgrounded().getBlocking();
    }

    /**
     * Emits backgrounded state.
     */
    public static Observable<Boolean> get() {

        // Return non-null distinct backgrounded events.
        return MGBackgroundedConfig.getBackgrounded().get(false).distinctUntilChanged();
    }
}