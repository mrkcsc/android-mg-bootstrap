package com.miguelgaeta.bootstrap.mg_websocket.events;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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

    private String message;

    public Observable<JsonElement> fromJson() {

        return Observable.create(subscriber -> {

            subscriber.onNext(new JsonParser().parse(message));
            subscriber.onCompleted();
        });
    }

    public <T> Observable<T> fromJson(TypeToken typeToken) {

        return Observable.create(subscriber -> {

            T json = new Gson().fromJson(message, typeToken.getType());

            subscriber.onNext(json);
            subscriber.onCompleted();
        });
    }

    public <T> Observable<T> fromJson(Class<T> clazz) {

        return Observable.create(subscriber -> {

            T json = new Gson().fromJson(message, clazz);

            subscriber.onNext(json);
            subscriber.onCompleted();
        });
    }
}
