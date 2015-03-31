package com.miguelgaeta.bootstrap.mg_websocket;

import android.support.annotation.NonNull;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/10/15.
 */
@SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
public class MGWebsocket {

    public enum STATE {
        NOT_YET_CONNECTED, CONNECTING, OPEN, CLOSING, CLOSED
    }

    @Getter(lazy = true)
    private final MGWebsocketConfig config = new MGWebsocketConfig();

    @Getter(AccessLevel.PACKAGE) private final PublishSubject<MGWebsocketData.Open>    onO = PublishSubject.create();
    @Getter(AccessLevel.PACKAGE) private final PublishSubject<MGWebsocketData.Closed>  onC = PublishSubject.create();
    @Getter(AccessLevel.PACKAGE) private final PublishSubject<MGWebsocketData.Message> onM = PublishSubject.create();
    @Getter(AccessLevel.PACKAGE) private final PublishSubject<MGWebsocketData.Error>   onE = PublishSubject.create();

    @Getter private final Observable<MGWebsocketData.Open>    onOpen    = onO.asObservable();
    @Getter private final Observable<MGWebsocketData.Closed>  onClose   = onC.asObservable();
    @Getter private final Observable<MGWebsocketData.Message> onMessage = onM.asObservable();
    @Getter private final Observable<MGWebsocketData.Error>   onError   = onE.asObservable();

    @Getter(AccessLevel.PACKAGE)
    private final List<String> messageBuffer = new ArrayList<>();

    @Getter(AccessLevel.PACKAGE)
    private WebSocketClient client;

    /**
     * This is the state we want the client to be in. If
     * reconnecting, will always try to achieve this state.
     */
    @Getter(AccessLevel.PACKAGE)
    private STATE clientDesiredState = STATE.NOT_YET_CONNECTED;

    /**
     * Public constructor disallowed.
     */
    MGWebsocket() { }

    /**
     * Create new websocket.
     */
    public static MGWebsocket create() {

        return MGWebsocketUtil.create();
    }

    /**
     * Open socket.
     */
    public void connect() {

        if (client == null) {
            client = MGWebsocketUtil.createWebsocketClient(getConfig().getUrl(), onO, onC, onM, onE);
        }

        client.connect();
        clientDesiredState = STATE.OPEN;
    }

    /**
     * Close socket.
     */
    public void close() {

        if (client != null) {
            client.close();
            clientDesiredState = STATE.CLOSED;
        }
    }

    /**
     * Send a message (buffer the message if not connected).
     */
    public void message(@NonNull String message) {

        if (client != null && client.getReadyState() == WebSocket.READYSTATE.OPEN) {
            client.send(message);

        } else if (getConfig().isBuffered()) {

            // Send when connected.
            messageBuffer.add(message);
        }
    }

    /**
     * Get the state of the web socket.
     */
    public STATE getState() {

        return MGWebsocketUtil.getWebsocketClientState(client);
    }
}
