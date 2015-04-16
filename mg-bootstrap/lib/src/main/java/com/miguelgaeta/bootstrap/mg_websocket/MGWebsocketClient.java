package com.miguelgaeta.bootstrap.mg_websocket;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClientSSL;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by mrkcsc on 4/15/15.
 */
class MGWebsocketClient {

    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventOpened>  eventOpn = MGPreferenceRx.create("WEBSOCKET_EVENTS_OPN", null, false);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventClosed>  eventCls = MGPreferenceRx.create("WEBSOCKET_EVENTS_CLS", null, false);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventMessage> eventMsg = MGPreferenceRx.create("WEBSOCKET_EVENTS_MSG", null, false);
    @Getter(lazy = true) private final MGPreferenceRx<MGWebsocketEventError>   eventErr = MGPreferenceRx.create("WEBSOCKET_EVENTS_ERR", null, false);

    @Getter(AccessLevel.PACKAGE)
    private final List<String> messageBuffer = new ArrayList<>();

    // Raw websocket object.
    private WebSocketClient client;

    // Optional reconnect delay.
    private Integer reconnectDelay;

    // Connect to url.
    private String url;

    // Subscription to keep alive observable.
    private Subscription heartBeatSubscription;

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

    public void connect(@NonNull String url, Integer reconnectDelay) {

        // Update url.
        this.url = url;

        // Update re-connect delay.
        this.reconnectDelay = reconnectDelay;

        client = createClient(url);
        client.connect();
        clientDesiredState = MGWebsocketState.OPENED;
    }

    public void disconnect() {

        if (client != null) {
            client.close();
            clientDesiredState = MGWebsocketState.CLOSED;
        }
    }

    public void message(@NonNull String message, boolean buffered) {

        if (client != null && client.getReadyState() == WebSocket.READYSTATE.OPEN) {
            client.send(message);

        } else if (buffered) {

            // Send when connected.
            messageBuffer.add(message);
        }
    }

    public void message(@NonNull Object message, boolean buffered) {

        Observable.create(subscriber -> {

            // Send serialized message.
            message(MGRestClient.getGson().toJson(message), buffered);

            // Done with observable.
            subscriber.onCompleted();
        })
            .subscribeOn(Schedulers.computation())
            .subscribe();
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
    private WebSocketClient createClient(@NonNull String url) {

        WebSocketClient client = new WebSocketClient(createURI(url)) {

            @Override
            public void onOpen(ServerHandshake handshakeData) {

                getEventCls().set(null);
                getEventErr().set(null);
                getEventOpn().set(MGWebsocketEventOpened.create(handshakeData.getHttpStatus(), handshakeData.getHttpStatusMessage()));
            }

            @Override
            public void onMessage(String message) {

                getEventMsg().set(MGWebsocketEventMessage.create(message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                getEventOpn().set(null);
                getEventErr().set(null);
                getEventCls().set(MGWebsocketEventClosed.create(code, reason, remote));
            }

            @Override
            public void onError(Exception ex) {

                getEventOpn().set(null);
                getEventCls().set(null);
                getEventErr().set(MGWebsocketEventError.create(ex));
            }
        };

        configureWSS(client, url);

        return client;
    }

    /**
     * Optionally enable insecure WSS for
     * urls that contain a WSS prefix.
     */
    private void configureWSS(@NonNull WebSocketClient client, @NonNull String url) {

        if (url.contains("wss")) {

            // Create an ssl socket factory with our all-trusting manager.
            SSLSocketFactory sslSocketFactory = MGRestClientSSL.createInsecureSSLSocketFactory();

            try {

                if (sslSocketFactory != null) {

                    // Provide SSL socket to the client.
                    client.setSocket(sslSocketFactory.createSocket());
                }

            } catch (IOException ignored) { }
        }
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
        });

        // If we disconnect, reconnect if needed.
        getEventCls().get(false).subscribe(data -> {

            if (shouldReconnect()) {

                MGDelay.delay(reconnectDelay).subscribe(r -> {

                    if (shouldReconnect()) {

                        connect(url, reconnectDelay);
                    }
                });
            }
        });
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
            });
        }
    }

    /**
     * Create a URI object from a
     * provided string.  Throws a runtime
     * exception if the url is invalid.
     */
    private static URI createURI(@android.support.annotation.NonNull String URL) {

        try {
            return new URI(URL);

        } catch (URISyntaxException ignored) {

            throw new RuntimeException("Unable to create URI from provided URL: " + URL);
        }
    }

    /**
     * Only reconnect if state mismatch, and reconnect
     * delay is specified.
     */
    private boolean shouldReconnect() {

        return clientDesiredState == MGWebsocketState.OPENED
            && (getState() == MGWebsocketState.CLOSED || getState() == MGWebsocketState.CLOSING) && reconnectDelay != null;
    }

    /**
     * Only disconnect if state mismatch.
     */
    private boolean shouldDisconnect() {

        return clientDesiredState == MGWebsocketState.CLOSED
            && (getState() == MGWebsocketState.OPENED || getState() == MGWebsocketState.CONNECTING);
    }
}
