package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGWebsocketConfig {

    /**
     * Should we buffer messages send to the web-socket -
     * by that we mean, save them while connection closed.
     */
    @Setter @Getter(AccessLevel.PACKAGE)
    private boolean buffered = true;

    /**
     * Should reconnect on errors.
     */
    @Setter @Getter(AccessLevel.PACKAGE)
    private boolean reconnect = true;

    /**
     * Time to wait before reconnect.
     */
    @Setter @Getter(AccessLevel.PACKAGE)
    private int reconnectDelay = 10000;

    /**
     * Url to connect to.
     */
    @Setter @Getter(AccessLevel.PACKAGE)
    private String url;
}
