package com.miguelgaeta.bootstrap.mg_websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 5/19/15.
 *
 * http://bit.ly/1R1lhD7
 *
 * Has some bugs for fetching ready state.  Has
 * a hack to fix it.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class MGWebsocketClientBase extends WebSocketClient {

    // Fix bug in the client.
    private READYSTATE readystate = null;

    public MGWebsocketClientBase(URI serverURI) {
        super(serverURI);
    }

    public MGWebsocketClientBase(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public MGWebsocketClientBase(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    public MGWebsocketClientBase(String serverURL) {
        super(createURI(serverURL));
    }

    public MGWebsocketClientBase(String serverURL, Draft draft) {
        super(createURI(serverURL), draft);
    }

    public MGWebsocketClientBase(String serverURL, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(createURI(serverURL), protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public READYSTATE getReadyState() {

        if (readystate != null) {

            return readystate;
        }

        return super.getReadyState();
    }

    @Override
    public final void onOpen(ServerHandshake serverHandshake) {

        readystate = READYSTATE.OPEN;

        onDidOpen(serverHandshake);
    }

    @Override
    public final void onClose(int code, String reason, boolean remote) {

        readystate = READYSTATE.CLOSED;

        onDidClose(code, reason, remote);
    }

    @Override
    public final void onError(Exception exception) {

        readystate = READYSTATE.CLOSED;

        onDidError(exception);
    }

    @Override
    public void onMessage(ByteBuffer byteBuffer) {

        onDidMessage(null, byteBuffer);
    }

    @Override
    public final void onMessage(String message) {

        onDidMessage(message, null);
    }

    public abstract void onDidOpen(ServerHandshake serverHandshake);

    public abstract void onDidClose(int code, String reason, boolean remote);

    public abstract void onDidError(Exception exception);

    public abstract void onDidMessage(String message, ByteBuffer byteBuffer);

    private static URI createURI(@NonNull String URL) {

        try {
            return new URI(URL);

        } catch (URISyntaxException ignored) {

            throw new RuntimeException("Unable to create URI from provided URL: " + URL);
        }
    }
}
