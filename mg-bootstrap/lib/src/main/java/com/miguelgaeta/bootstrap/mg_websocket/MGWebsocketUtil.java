package com.miguelgaeta.bootstrap.mg_websocket;

import android.support.annotation.NonNull;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClientSSL;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLSocketFactory;

import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/11/15.
 */
class MGWebsocketUtil {

    /**
     * Configure dis-connect and re-connect
     * behavior for the web socket.
     */
    static void configureReconnect(@NonNull MGWebsocket websocket) {

        websocket.getOnO().subscribe(data -> {

            if (shouldDisconnect(websocket)) {

                // If we connect, but client wants to be
                // disconnected, close the web socket.
                MGDelay.delay(websocket.getConfig().getReconnectDelay()).subscribe(r -> {

                    if (shouldDisconnect(websocket)) {

                        websocket.close();
                    }
                });

            } else {

                for (String message : websocket.getMessageBuffer()) {

                    websocket.getClient().send(message);
                }

                // Flush the message buffer.
                websocket.getMessageBuffer().clear();
            }
        });

        // If we disconnect, reconnect if needed.
        websocket.getOnC().subscribe(data -> {

            if (shouldReconnect(websocket)) {

                MGDelay.delay(websocket.getConfig().getReconnectDelay()).subscribe(r -> {

                    if (shouldReconnect(websocket)) {

                        websocket.connect();
                    }
                });
            }
        });
    }

    /**
     * Creates a new web socket clients and
     * funnels events through RXJava publish subjects.
     */
    static WebSocketClient createWebsocketClient(
            @NonNull String url,
            @NonNull PublishSubject<MGWebsocketData.Open>    onO,
            @NonNull PublishSubject<MGWebsocketData.Closed>  onC,
            @NonNull PublishSubject<MGWebsocketData.Message> onM,
            @NonNull PublishSubject<MGWebsocketData.Error>   onE) {

        WebSocketClient client = new WebSocketClient(MGWebsocketUtil.createURI(url)) {

            @Override
            public void onOpen(ServerHandshake handshakeData) {

                onO.onNext(MGWebsocketData.Open.create(handshakeData.getHttpStatus(), handshakeData.getHttpStatusMessage()));
            }

            @Override
            public void onMessage(String message) {

                onM.onNext(MGWebsocketData.Message.create(message));
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                onC.onNext(MGWebsocketData.Closed.create(code, reason, remote));
            }

            @Override
            public void onError(Exception ex) {

                onE.onNext(MGWebsocketData.Error.create(ex));
            }
        };

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

        return client;
    }

    /**
     * Given a websocket client, get the state.
     */
    static MGWebsocket.STATE getWebsocketClientState(WebSocketClient client) {

        if (client == null) {

            return MGWebsocket.STATE.NOT_YET_CONNECTED;
        }

        switch (client.getReadyState()) {

            case CONNECTING:
                return MGWebsocket.STATE.CONNECTING;

            case OPEN:
                return MGWebsocket.STATE.OPEN;

            case CLOSING:
                return MGWebsocket.STATE.CLOSING;

            case CLOSED:
                return MGWebsocket.STATE.CLOSED;

            case NOT_YET_CONNECTED:
            default:
                return MGWebsocket.STATE.NOT_YET_CONNECTED;
        }
    }

    /**
     * Create URI to be used by web socket.
     */
    private static URI createURI(@NonNull String URL) {

        try {
            return new URI(URL);

        } catch (URISyntaxException ignored) {

            throw new RuntimeException("Unable to create URI from provided URL: " + URL);
        }
    }

    private static boolean shouldReconnect(@NonNull MGWebsocket websocket) {

        return websocket.getConfig().isReconnect() &&
               websocket.getClientDesiredState() == MGWebsocket.STATE.OPEN;
    }

    private static boolean shouldDisconnect(@NonNull MGWebsocket websocket) {

        return websocket.getConfig().isReconnect() &&
               websocket.getClientDesiredState() == MGWebsocket.STATE.CLOSED;
    }
}
