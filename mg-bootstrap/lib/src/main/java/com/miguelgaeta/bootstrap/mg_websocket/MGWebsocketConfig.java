package com.miguelgaeta.bootstrap.mg_websocket;

import com.google.gson.Gson;

import javax.net.ssl.SSLSocketFactory;

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
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private boolean buffered = true;

    /**
     * Time to wait before reconnect.  Set to
     * null if you do not want to re connect
     * on errors (unusual but sure).
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Integer reconnectDelay = 10000;

    /**
     * If set, this is the interval used to
     * ping the websocket to keep the
     * connection alive.
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Integer keepAliveInterval;

    /**
     * If keep alive is defined, will send
     * this message as the payload.
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private String keepAliveMessage;

    /**
     * Url to connect to.  Should be of the
     * prefix format ws:// or wss://
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private String url;

    /**
     * If using WSS web sockets allows
     * a socket factory to be provided.
     *
     * See: MGSSLFactory to create one.
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private SSLSocketFactory socketFactory;

    /**
     * Used for serializing messages. Not
     * required as user can pass in
     * their own gson object.
     */
    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Gson gson;
}
