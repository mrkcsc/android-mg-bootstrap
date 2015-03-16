package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGWebsocketConfig {

    /**
     * Should we buffer messages send to the web-socket -
     * by that we mean, save them while connection closed.
     */
    @Getter
    private boolean buffered = true;

    /**
     * Should reconnect on errors.
     */
    @Getter
    private boolean reconnect = true;

    /**
     * Url to connect to.
     */
    @Getter
    private String url;
}
