package com.miguelgaeta.bootstrap.mg_comet;

import android.os.Handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClientSSL;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Miguel on 7/17/2014. Copyright 2014 Blitz Studios
 */
class MGCometAPIWebsocket {

    // region Member Variables
    // ============================================================================================================

    // Connection status.
    private boolean mWebSocketConnected;
    private boolean mWebSocketConnecting;

    // User to ping the socket for keep-alive.
    private Handler          mWebSocketPingHandler;
    private final static int mWebSocketPingInterval = 60000;

    // Time to reconnect on failures.
    private Handler          mWebSocketReconnectHandler;
    private boolean          mWebSocketReconnectPending;
    private final static int mWebSocketReconnectInterval = 30000;

    // Web socket client.
    private WebSocketClient mWebSocketClient;

    // Messages that need to be sent when connected.
    private ArrayList<String> mPendingWebSocketMessages;

    // Interface for receiving messages.
    private OnMessageCallback mOnMessageCallback;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Empty constructor disallowed.
     */
    @SuppressWarnings("unused")
    private MGCometAPIWebsocket() {

    }

    /**
     * Default constructor.
     */
    @SuppressWarnings("unused")
    public MGCometAPIWebsocket(OnMessageCallback callback) {

        // Initialize pending messages.
        mPendingWebSocketMessages = new ArrayList<>();

        // Set callback.
        mOnMessageCallback = callback;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Open the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public void openWebSocket()  {

        if (!mWebSocketConnected) {

            // Create a client every time.
            createWebSocketClient();

            // Send connect command.
            mWebSocketClient.connect();
            mWebSocketConnecting = true;
        }

        // Enable re-connect.
        enableWebSocketReconnect();

        // Enable pinging.
        enableWebSocketPings();
    }

    /**
     * Close the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public void closeWebSocket() {

        if (mWebSocketConnected &&
            mWebSocketClient != null) {

            // Send close command.
            mWebSocketClient.close();
            mWebSocketConnecting = false;
        }

        // Disable re-connect.
        disableWebSocketReconnect();

        // Disable pinging.
        disableWebSocketPings();
    }

    /**
     * Sends a message to the socket.
     *
     * @param jsonMessage Message should be in json format.
     */
    @SuppressWarnings("unused")
    public void sendMessageToWebSocket(String jsonMessage) {

        if (mWebSocketConnected) {

            try {
                // Send message to the web socket.
                mWebSocketClient.send(jsonMessage);

            } catch (Exception exception) {

                // Add to pending if not connected.
                mPendingWebSocketMessages.add(jsonMessage);

                // Try to re-connect.
                cleanupWebSocket(true);
            }
        } else {

            // Add to pending if not connected.
            mPendingWebSocketMessages.add(jsonMessage);
        }
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Create an instance of the WebSocket client.
     */
    private void createWebSocketClient() {

        // Fetch URI from provided URL.
        URI webSocketURI = createWebSocketURI("some url"); // TODO: Configurable URL.

        // Initialize the client.
        mWebSocketClient = new WebSocketClient(webSocketURI) {

            /**
             * When a message is received, send it
             * through the callback interface.
             *
             * @param message Message assumed to be a JSON string.
             */
            @Override
            public void onMessage(String message) {

                // Send message via callback.
                if (mOnMessageCallback != null) {
                    mOnMessageCallback.onMessage(new JsonParser().parse(message).getAsJsonObject());
                }
            }

            @Override
            public void onOpen(ServerHandshake serverHandshake) {

                // Now connected.
                mWebSocketConnected = true;

                // Iterate over any pending messages.
                for (String jsonMessage : mPendingWebSocketMessages) {

                    // Send to web socket.
                    sendMessageToWebSocket(jsonMessage);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                // Cleanup connection.
                cleanupWebSocket(mWebSocketConnecting);
            }

            @Override
            public void onError(Exception e) {

                // Cleanup connection.
                cleanupWebSocket(mWebSocketConnecting);
            }
        };

        // TODO: Hook up SSL.

        try {
            mWebSocketClient.setSocket(MGRestClientSSL.createInsecureSSLSocketFactory().createSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleanup web socket on close or error.
     *
     * @param reconnect Should we attempt to re-connect.
     */
    private void cleanupWebSocket(boolean reconnect) {

        // Not connected.
        mWebSocketConnected = false;

        // Set a pending re-connect if requested.
        mWebSocketReconnectPending = reconnect;
    }

    /**
     * Enable re-connecting to the web socket
     * after a specified delay.
     */
    private void enableWebSocketReconnect() {

        /*
        mWebSocketReconnectHandler = BlitzDelay.postDelayed(new Runnable() {

            @Override
            public void run() {

                // Re-connect if pending.
                if (mWebSocketReconnectPending) {
                    mWebSocketReconnectPending = false;

                    // Open connection.
                    openWebSocket();
                }
            }
        }, mWebSocketReconnectInterval, true);
        */
    }

    /**
     * Disable re-connecting to the web socket.
     */
    private void disableWebSocketReconnect() {

        // Remove any pending re-connect callbacks.
        //BlitzDelay.remove(mWebSocketReconnectHandler);

        // Disable pending re-connect.
        mWebSocketReconnectPending = false;
    }

    /**
     * Given a URL convert to a URI.
     *
     * @param webSocketURL Target URL.
     *
     * @return URI or null on failure.
     */
    private URI createWebSocketURI(String webSocketURL) {

        try {

            // Create from AppConfig URL.
            return new URI(webSocketURL);

        } catch (URISyntaxException ignored) { }

        return null;
    }

    /**
     * Enable pinging of the web socket.
     */
    private void enableWebSocketPings() {

        /*
        mWebSocketPingHandler = BlitzDelay.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (mWebSocketConnected) {

                    // Send a ping if connected.
                    sendMessageToWebSocket("ping");
                }
            }
        }, mWebSocketPingInterval, true);
        */
    }

    /**
     * Disable pinging of the web socket.
     */
    private void disableWebSocketPings() {

        // Remove callbacks which stops the ping cycle.
        //BlitzDelay.remove(mWebSocketPingHandler);
    }

    // endregion

    // region Interface
    // ============================================================================================================

    /**
     * Callback for when a websocket
     * message is received.
     */
    interface OnMessageCallback {

        public void onMessage(JsonObject jsonObject);
    }

    // endregion
}