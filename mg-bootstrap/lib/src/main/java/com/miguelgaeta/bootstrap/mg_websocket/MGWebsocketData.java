package com.miguelgaeta.bootstrap.mg_websocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import rx.Observable;

/**
 * Created by mrkcsc on 3/11/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGWebsocketData {

    // Private constructor.
    private MGWebsocketData() { }

    @AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
    public static class Open {

        // Http status code.
        private short httpStatus;

        // Http status message.
        private String httpStatusMessage;
    }

    @AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
    public static class Closed {

        // Status code.
        private int code;

        // Closed reason.
        private String reason;

        // Remotely closed.
        private boolean remote;
    }

    @AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
    public static class Message {

        // Raw message.
        private String message;

        /**
         * Fetch the message string
         * as a json element.
         */
        public Observable<JsonElement> getMessageJson() {

            return Observable.create(subscriber -> {

                subscriber.onNext(new JsonParser().parse(message));
                subscriber.onCompleted();
            });
        }

        /**
         * Fetch the message string
         * as a serialized JSON object.
         */
        public <T> Observable<T> getMessageJson(Class<T> classOfT) {

            return Observable.create(subscriber -> {

                subscriber.onNext(new Gson().fromJson(message, classOfT));
                subscriber.onCompleted();
            });
        }
    }

    @AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
    public static class Error {

        // Exception.
        private Exception ex;
    }
}
