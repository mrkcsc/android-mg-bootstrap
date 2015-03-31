package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by mrkcsc on 3/11/15.
 */
public class MGWebsocketData {

    // Private constructor.
    private MGWebsocketData() { }

    @Data @AllArgsConstructor(staticName = "create")
    public static class Open {

        // Http status code.
        private short httpStatus;

        // Http status message.
        private String httpStatusMessage;
    }

    @Data @AllArgsConstructor(staticName = "create")
    public static class Closed {

        // Status code.
        private int code;

        // Closed reason.
        private String reason;

        // Remotely closed.
        private boolean remote;
    }

    @Data @AllArgsConstructor(staticName = "create")
    public static class Message {

        // Raw message.
        private String message;
    }

    @Data @AllArgsConstructor(staticName = "create")
    public static class Error {

        // Exception.
        private Exception ex;
    }
}
