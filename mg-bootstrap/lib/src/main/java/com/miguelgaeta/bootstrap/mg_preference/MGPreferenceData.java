package com.miguelgaeta.bootstrap.mg_preference;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;

import lombok.RequiredArgsConstructor;
import rx.Subscription;
import rx.functions.Action1;

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
    private long serializationTimerStart;

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

        if (serializationTimerStart == 0) {
            serializationTimerStart = System.currentTimeMillis();
        }

        if ((System.currentTimeMillis() - serializationTimerStart) > serializationDelay) {

            set();

        } else {

            delayedSerialization =
                MGDelay
                    .delay(serializationDelay)
                    .observeOn(MGPreference.getScheduler())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            set();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            MGPreference
                                .getErrorHandler()
                                .call(new MGPreference.Error("Unable to serialize preference.", throwable));
                        }
                    });
        }
    }

    private void set() {
        serializationTimerStart = 0;

        MGPreference.getDataStore().set(key, value);

        delayedSerialization = null;
    }

    public void clear() {

        set(defaultValue);
    }
}
