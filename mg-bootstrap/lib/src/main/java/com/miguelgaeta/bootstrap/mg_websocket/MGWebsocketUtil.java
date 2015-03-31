package com.miguelgaeta.bootstrap.mg_websocket;

import android.support.annotation.NonNull;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 3/11/15.
 */
class MGWebsocketUtil {

    // TODO: Flesh out.
    static MGWebsocket create() {

        MGWebsocket websocket = new MGWebsocket();

        websocket.getOnO().subscribe(data -> {

            // TODO: Add delay.
            if (websocket.getConfig().isReconnect() &&
                websocket.getClientDesiredState() == MGWebsocket.STATE.CLOSED) {
                websocket.close();
            } else {

                // Flush the message buffer.
                for (String message : websocket.getMessageBuffer()) {

                    websocket.getClient().send(message);
                }
            }
        });

        // TODO: Add received message buffering.

        websocket.getOnC().subscribe(data -> {

            // TODO: Add delay.
            if (websocket.getConfig().isReconnect() &&
                websocket.getClientDesiredState() == MGWebsocket.STATE.OPEN) {
                websocket.connect();
            }
        });

        websocket.getOnE().subscribe(data -> {

            // TODO: Add delay.
            if (websocket.getConfig().isReconnect() &&
                websocket.getClientDesiredState() == MGWebsocket.STATE.OPEN) {
                websocket.connect();
            }
        });

        return websocket;
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

        return new WebSocketClient(MGWebsocketUtil.createURI(url)) {

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
}
