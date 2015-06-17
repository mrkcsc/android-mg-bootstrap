package com.miguelgaeta.bootstrap.mg_websocket;

import com.google.gson.Gson;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventClosed;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventError;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventMessage;
import com.miguelgaeta.bootstrap.mg_websocket.events.MGWebsocketEventOpened;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import rx.Observable;
import rx.Subscription;

/**
 * Created by mrkcsc on 4/15/15.
 */
class MGWebsocketClient {

    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventOpened>  eventOpn = MGPreferenceRx.create(null);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventClosed>  eventCls = MGPreferenceRx.create(null);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventMessage> eventMsg = MGPreferenceRx.create(null);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventError>   eventErr = MGPreferenceRx.create(null);

    @Getter(AccessLevel.PACKAGE)
    private final List<String> messageBuffer = new ArrayList<>();

    // Raw websocket object.
    private MGWebsocketClientBase client;

    // Optional reconnect delay.
    private Integer reconnectDelay;

    // Connect to url.
    private String url;

    // Subscription to keep alive observable.
    private Subscription heartBeatSubscription;

    // Optionally used for SSL.
    private SSLSocketFactory socketFactory;

    /**
     * This is the state we want the client to be in. If
     * reconnecting, will always try to achieve this state.
     */
    private MGWebsocketState clientDesiredState = MGWebsocketState.NOT_YET_CONNECTED;

    /**
     * Default constructor.
     */
    public MGWebsocketClient() {

        // Reconnection logic is powered by observable
        // events to only register once per instance.
        configureReconnect();
    }

    /**
     * Connect to the web socket given a
     * specified url.  Optionally provide a
     * specified reconnection delay.=
     */
    public void connect(@NonNull String url, Integer reconnectDelay, SSLSocketFactory socketFactory) {

        this.url = url;
        this.reconnectDelay = reconnectDelay;
        this.socketFactory = socketFactory;

        client = createClient();
        client.connect();
        clientDesiredState = MGWebsocketState.OPENED;
    }

    /**
     * Close the socket.
     */
    public void disconnect() {

        // Don't need to call close if already happened or happening.
        boolean closed =
            getState() == MGWebsocketState.CLOSED  ||
            getState() == MGWebsocketState.CLOSING ||
            getState() == MGWebsocketState.NOT_YET_CONNECTED;

        if (client != null && !closed) {
            client.close();
            clientDesiredState = MGWebsocketState.CLOSED;
        }
    }

    /**
     * Send a string to the web socket.  If
     * buffer flag is set, send the message as
     * soon as web socket is connected.
     */
    public void message(@NonNull String message, boolean buffered) {

        try {

            if (client != null && client.getReadyState() == WebSocket.READYSTATE.OPEN) {
                client.send(message);

            } else if (buffered) {

                messageBuffer.add(message);
            }

        } catch (WebsocketNotConnectedException e) {

            if (buffered) {

                messageBuffer.add(message);
            }
        }
    }

    /**
     * Send an arbitrary object as a json string.
     */
    public void messageJson(@NonNull Object message, @NonNull Gson gson, boolean buffered) {

        Observable.create(subscriber -> {

            // Send serialized message.
            message(gson.toJson(message), buffered);

            // Done with observable.
            subscriber.onCompleted();

        }).subscribe(r -> { }, MGRxError.create(null, "Unable to send message."));
    }

    /**
     * Provides new heartbeat parameters.
     */
    public void heartBeat(Integer heartBeatInterval, String message) {

        // If the socket is opened, try to keep it
        // open by sending keep alive messages on
        // an interval defined by the user.
        configureHeartbeat(heartBeatInterval, message);
    }

    /**
     * Returns the current state of the
     * web socket client.
     */
    public MGWebsocketState getState() {

        if (client == null) {

            return MGWebsocketState.NOT_YET_CONNECTED;
        }

        switch (client.getReadyState()) {

            case CONNECTING:
                return MGWebsocketState.CONNECTING;

            case OPEN:
                return MGWebsocketState.OPENED;

            case CLOSING:
                return MGWebsocketState.CLOSING;

            case CLOSED:
                return MGWebsocketState.CLOSED;

            case NOT_YET_CONNECTED:
            default:
                return MGWebsocketState.NOT_YET_CONNECTED;
        }
    }

    /**
     * Configures the native websocket
     * client implementation used by
     * the wrapper.
     */
    private MGWebsocketClientBase createClient() {

        MGWebsocketClientBase client = new MGWebsocketClientBase(url) {

            @Override
            public void onDidOpen(ServerHandshake handshakeData) {

                getEventCls().set(null);
                getEventErr().set(null);
                getEventOpn().set(MGWebsocketEventOpened.create(handshakeData.getHttpStatus(), handshakeData.getHttpStatusMessage()));
            }

            @Override
            public void onDidMessage(String message) {

                getEventMsg().set(MGWebsocketEventMessage.create(message));
            }

            @Override
            public void onDidClose(int code, String reason, boolean remote) {

                getEventOpn().set(null);
                getEventErr().set(null);
                getEventCls().set(MGWebsocketEventClosed.create(code, reason, remote));
            }

            @Override
            public void onDidError(Exception exception) {

                getEventOpn().set(null);
                getEventCls().set(null);
                getEventErr().set(MGWebsocketEventError.create(exception));
            }
        };

        if (socketFactory != null) {

            try {

                // Provide SSL socket to the client.
                client.setSocket(socketFactory.createSocket());

            } catch (IOException e) {

                MGLog.e(e, "Unable to create socket with WSS.");
            }
        }

        return client;
    }

    /**
     * Configure dis-connect and re-connect
     * behavior for the web socket.
     */
    private void configureReconnect() {

        getEventOpn().get(false).subscribe(data -> {

            // If we connect, but client wants to be disconnected.
            if (shouldDisconnect()) {

                // Disconnect the web socket.
                disconnect();

            } else {

                for (String message : messageBuffer) {

                    client.send(message);
                }

                // Flush the message buffer.
                messageBuffer.clear();
            }

        }, MGRxError.create(null, "Unable to open."));

        // If we disconnect, reconnect if needed.
        getEventCls().get(false).subscribe(data -> {

            if (shouldReconnect()) {

                MGDelay.delay(reconnectDelay).subscribe(r -> {

                    if (shouldReconnect()) {

                        connect(url, reconnectDelay, socketFactory);
                    }

                }, MGRxError.create(null, "Unable to re-connect."));
            }

        }, MGRxError.create(null, "Unable to close."));
    }

    /**
     * Cancels any existing heartbeat and if an
     * interval if provided, starts sending new
     * heartbeat messages while connection open.
     */
    private void configureHeartbeat(Integer keepAliveInterval, String keepAliveMessage) {

        if (heartBeatSubscription != null) {
            heartBeatSubscription.unsubscribe();
        }

        if (keepAliveInterval != null) {

            heartBeatSubscription = MGDelay.delay(keepAliveInterval, true).subscribe(aVoid -> {

                if (getState() == MGWebsocketState.OPENED) {

                    // Send keep alive message.
                    message(keepAliveMessage, false);
                }

            }, MGRxError.create(null, "Unable to send heartbeat."));
        }
    }

    /**
     * Only reconnect if state mismatch, and reconnect
     * delay is specified.
     */
    private boolean shouldReconnect() {

        return clientDesiredState == MGWebsocketState.OPENED
            && (getState() == MGWebsocketState.CLOSED  ||
                getState() == MGWebsocketState.CLOSING ||
                getState() == MGWebsocketState.NOT_YET_CONNECTED) && reconnectDelay != null;
    }

    /**
     * Only disconnect if state mismatch.
     */
    private boolean shouldDisconnect() {

        return clientDesiredState == MGWebsocketState.CLOSED
            && (getState() == MGWebsocketState.OPENED || getState() == MGWebsocketState.CONNECTING);
    }
}
