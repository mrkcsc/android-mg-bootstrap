package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.RequiredArgsConstructor;
import rx.Observable;

/**
 * Created by mrkcsc on 3/9/15.
 */
@RequiredArgsConstructor(staticName = "create")
class MGPreferenceData<T> {

    private final String key;

    private final TypeToken<?> typeToken;

    private final T defaultValue;

    private T value;

    private final boolean global;

    private final Gson gson = MGRestClient.getGson();

    public T get() {

        if (value == null) {

            String valueJson = MGPreference.getDataStore().get(key, global);

            if (valueJson != null) {

                value = gson.fromJson(valueJson, typeToken.getType());
            }
        }

        if (value == null && defaultValue != null) {

            return defaultValue;
        }

        return value;
    }

    public void set(T value) {

        this.value = value;

        Observable.just(null).observeOn(MGPreference.getScheduler()).subscribe(r -> MGPreference.getDataStore().set(key, serializeValue(), global),
            MGRxError.create(null, "Unable to serialize preference."));
    }

    private String serializeValue() {

        return value == null ? null : gson.toJson(value, typeToken.getType());
    }
}
