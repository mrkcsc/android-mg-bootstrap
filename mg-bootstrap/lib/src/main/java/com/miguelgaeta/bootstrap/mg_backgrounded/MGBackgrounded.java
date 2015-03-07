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

    @Getter (lazy = true)
    private static final Observable<Boolean> observable
            = MGBackgroundedConfig.getBackgrounded().distinctUntilChanged().cache(1);

    /**
     * Is the application currently in the background.  "In the background"
     * is defined as when we have left the foreground for more than one second.
     */
    public static boolean isBackgrounded() {

        // Fetch the most recent backgrounded value.
        return getObservable().toBlocking().mostRecent(false).iterator().next();
    }
}