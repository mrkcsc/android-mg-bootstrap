package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
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

    private final boolean versioned;

    private Subscription delayedSerialization;

    private final Gson gson = MGRestClient.getGson();

    /**
     * If value is not already in memory, attempt to fetch
     * it from the data store.  If still cannot be
     * found, emit the default value.
     */
    @SuppressWarnings("unchecked")
    public T get() {

        if (value == null && delayedSerialization == null) {
            value = (T)MGPreference.getDataStore().get(key, typeToken.getRawType(), versioned) ;

            if (value == null && defaultValue != null) {

                return defaultValue;
            }
        }

        return value;
    }

    /**
     * Updates value in memory and also persists to cache.  If versioned
     * field, persist on delay, otherwise persist immediately.  This is
     * not really a very good heuristic so should be revised to happen in a
     * more intelligent fashion.
     *
     * TODO: Delay flag should be configurable and off by default.
     */
    public void set(T value) {

        this.value = value;

        if (delayedSerialization != null) {
            delayedSerialization.unsubscribe();
        }

        if (versioned) {

            delayedSerialization = MGDelay.delay(SERIALIZATION_DELAY).observeOn(MGPreference.getScheduler()).subscribe(r -> {

                MGPreference.getDataStore().set(key, value, typeToken.getType(), true);

                delayedSerialization = null;

            }, MGRxError.create(null, "Unable to serialize preference."));

        } else {

            MGPreference.getDataStore().set(key, value, typeToken.getType(), false);
        }
    }

    public void clear() {

        set(defaultValue);
    }
}
