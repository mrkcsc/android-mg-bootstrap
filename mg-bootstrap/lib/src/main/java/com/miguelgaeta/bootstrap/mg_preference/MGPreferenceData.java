package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.RequiredArgsConstructor;
import rx.Subscription;

/**
 * Created by mrkcsc on 3/9/15.
 */
@RequiredArgsConstructor(staticName = "create")
class MGPreferenceData<T> {

    /**
     * Standard delay to prevent stacked serialize calls
     * from using up too much memory and OOM.
     */
    private static final int SERIALIZATION_DELAY = 10000;

    private final String key;

    private final TypeToken<?> typeToken;

    private final T defaultValue;

    private T value;

    private final boolean global;

    private Subscription delayedSerialization;

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

        if (delayedSerialization != null) {
            delayedSerialization.unsubscribe();
        }

        delayedSerialization = MGDelay.delay(SERIALIZATION_DELAY).observeOn(MGPreference.getScheduler()).subscribe(r -> {

            // Make sure we don't serialize too fast.
            MGPreference.getDataStore().set(key, serializeValue(), global);

        }, MGRxError.create(null, "Unable to serialize preference."));
    }

    private String serializeValue() {

        try {

            return value == null ? null : gson.toJson(value, typeToken.getType());

        } catch (OutOfMemoryError e) {

            // Alert of any preferences that become too large.
            MGLog.e("Out of memory while trying to serialize: " + key);

            throw e;
        }
    }
}
