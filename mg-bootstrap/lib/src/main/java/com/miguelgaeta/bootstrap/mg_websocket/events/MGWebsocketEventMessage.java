package com.miguelgaeta.bootstrap.mg_websocket.events;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import rx.Observable;

/**
 * Created by mrkcsc on 4/15/15.
 */
@AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
public class MGWebsocketEventMessage {

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
