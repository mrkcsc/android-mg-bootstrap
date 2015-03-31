package com.miguelgaeta.bootstrap.mg_websocket;

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
    @Setter @Getter
    private boolean buffered = true;

    /**
     * Should reconnect on errors.
     */
    @Setter @Getter
    private boolean reconnect = true;

    /**
     * Url to connect to.
     */
    @Setter @Getter
    private String url;
}
