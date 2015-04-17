package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/10/15.
 */
@SuppressWarnings("UnusedDeclaration") @Setter @Getter(AccessLevel.PACKAGE)
public class MGWebsocketConfig {

    /**
     * Should we buffer messages send to the web-socket -
     * by that we mean, save them while connection closed.
     */
    private boolean buffered = true;

    /**
     * Time to wait before reconnect.  Set to
     * null if you do not want to re connect
     * on errors (unusual but sure).
     */
    private Integer reconnectDelay = 10000;

    /**
     * If set, this is the interval used to
     * ping the websocket to keep the
     * connection alive.
     */
    private Integer keepAliveInterval;

    /**
     * If keep alive is defined, will send
     * this message as the payload.
     */
    private String keepAliveMessage;

    /**
     * Url to connect to.  Should be of the
     * prefix format ws:// or wss://
     */
    private String url;
}
