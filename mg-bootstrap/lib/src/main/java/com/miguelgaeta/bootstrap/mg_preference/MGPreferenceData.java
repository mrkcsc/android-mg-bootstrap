package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.RequiredArgsConstructor;
import rx.Observable;
import rx.functions.Action1;

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

    /**
     * If value is not already in memory, attempt to fetch
     * it from the data store.  If still cannot be
     * found, emit the default value.
     */
    public T get() {

        if (value == null) {
            value = gson.fromJson(MGPreference.getDataStore().get(key, global), typeToken.getType());

            if (value == null && defaultValue != null) {

                return defaultValue;
            }
        }

        return value;
    }

    /**
     * Update the value and also persist to the
     * data store in the background.
     */
    public void set(T value) {

        this.value = value;

        persist(o -> MGPreference.getDataStore().set(key, serializeValue(), global));
    }

    private void persist(Action1<Object> callback) {

        Observable.just(null).observeOn(MGPreference.getScheduler()).subscribe(callback, MGRxError.create(null, "Unable to serialize preference."));
    }

    private String serializeValue() {

        return value == null ? null : gson.toJson(value, typeToken.getType());
    }
}
