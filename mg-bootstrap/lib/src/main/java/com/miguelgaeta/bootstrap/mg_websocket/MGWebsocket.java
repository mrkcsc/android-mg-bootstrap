package com.miguelgaeta.bootstrap.mg_websocket;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventClosed;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventError;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventMessage;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventOpened;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;

/**
 * Created by mrkcsc on 3/10/15.
 */
@SuppressWarnings({"UnusedDeclaration", "MismatchedQueryAndUpdateOfCollection"})
public class MGWebsocket {

    @Getter(lazy = true)
    private final MGWebsocketConfig config = new MGWebsocketConfig();

    @Getter(AccessLevel.PACKAGE)
    private final MGWebsocketClient client = new MGWebsocketClient();

    /**
     * Open socket.
     */
    public void connect() {

        client.connect(getConfig().getUrl(), getConfig().getReconnectDelay(), getConfig().getSocketFactory());
    }

    /**
     * Send a kill signal to the web
     * socket - does not happen sync.
     */
    public void disconnect() {

        client.disconnect();
    }

    /**
     * Send a message (buffer the message if not connected).
     */
    public void message(@NonNull String message) {

        client.message(message, getConfig().isBuffered());
    }

    /**
     * Send a message but serializes the object
     * before sending it.
     */
    public void messageJson(@NonNull Object message) {

        client.messageJson(message, MGRestClient.getGson(), getConfig().isBuffered());
    }

    /**
     * Send a message but serializes the object
     * before sending it.
     */
    public void messageJson(@NonNull Object message, @NonNull Gson gson) {

        client.messageJson(message, gson, getConfig().isBuffered());
    }

    /**
     * Set a keep alive message for the web socket. Provide
     * null values to disable.  Interval is
     * defined in minutes.
     */
    public void heartBeat(Integer interval, String message) {

        client.heartBeat(interval, message);
    }

    /**
     * Provides new heartbeat parameters,  accepts
     * an object tht will be serialized to json.
     */
    public void heartBeat(Integer interval, Object message) {

        client.heartBeat(interval, MGRestClient.getGson().toJson(message));
    }

    /**
     * Get the current state of the web
     * socket (connected, connecting, etc).
     */
    public MGWebsocketState getState() {

        return client.getState();
    }

    /**
     * Emits an event only when the web
     * socket is opened.
     */
    public Observable<MGWebsocketEventOpened> onOpened() {

        return client.getEventOpn().get(false);
    }

    /**
     * Emits an event only when the web
     * socket is closed.
     */
    public Observable<MGWebsocketEventClosed> onClosed() {

        return client.getEventCls().get(false);
    }

    /**
     * Emits an event only when the web
     * socket has recieved an error.
     */
    public Observable<MGWebsocketEventError> onError() {

        return client.getEventErr().get(false);
    }

    /**
     * Emits the last message recieved by
     * the web socket while it is open.
     */
    public Observable<MGWebsocketEventMessage> onMessage() {

        return client.getEventMsg().get(false);
    }
}
