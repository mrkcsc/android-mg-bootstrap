package com.miguelgaeta.bootstrap.mg_preference;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.RequiredArgsConstructor;
import rx.Subscription;

/**
 * Created by mrkcsc on 3/9/15.
 */
@RequiredArgsConstructor(staticName = "create")
class MGPreferenceData<T> {

    private final String key;

    private final T defaultValue;

    /**
     * In memory value of the preference object to
     * minimize reads from file system.
     */
    private T value;

    /**
     * In some situation this value should be set to larger
     * values if it's a fast emitting data stream.
     */
    private final int serializationDelay;

    private Subscription delayedSerialization;

    /**
     * If value is not already in memory, attempt to fetch
     * it from the data store.  If still cannot be
     * found, emit the default value.
     */
    @SuppressWarnings("unchecked")
    public T get() {

        if (value == null && delayedSerialization == null) {
            value = (T)MGPreference.getDataStore().get(key);

            if (value == null && defaultValue != null) {

                return defaultValue;
            }
        }

        return value;
    }

    /**
     * Updates value in memory and also persists to cache.
     */
    public void set(T value) {

        this.value = value;

        if (delayedSerialization != null) {
            delayedSerialization.unsubscribe();
        }

        delayedSerialization = MGDelay.delay(serializationDelay).observeOn(MGPreference.getScheduler()).subscribe(r -> {

            MGPreference.getDataStore().set(key, value);

            delayedSerialization = null;

        }, MGRxError.create(null, "Unable to serialize preference."));
    }

    public void clear() {

        set(defaultValue);
    }
}
