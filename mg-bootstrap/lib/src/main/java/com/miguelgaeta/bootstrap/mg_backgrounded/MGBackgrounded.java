package com.miguelgaeta.bootstrap.mg_backgrounded;

import java.util.concurrent.TimeUnit;

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

        return getConfig().getBackgrounded().getBlocking();
    }

    /**
     * Emits backgrounded state which is guaranteed
     * to ne a non-null distinct until changed boolean.
     */
    public static Observable<Boolean> get() {

        return getConfig().getBackgrounded().get(false).distinctUntilChanged().throttleLast(100, TimeUnit.MILLISECONDS);
    }
}