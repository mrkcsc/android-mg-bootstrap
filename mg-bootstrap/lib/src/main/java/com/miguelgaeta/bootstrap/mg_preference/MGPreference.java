package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by mrkcsc on 12/5/14.
 */
@SuppressWarnings("unchecked, unused") @RequiredArgsConstructor
public class MGPreference<T> {

    @Getter(value = AccessLevel.PACKAGE, lazy = true)
    private static final MGPreferenceDataStore dataStore = new MGPreferenceDataStore();

    @Getter(value = AccessLevel.PACKAGE, lazy = true)
    private static final Scheduler scheduler = Schedulers.computation();

    @Getter(value = AccessLevel.PACKAGE, lazy = true)
    private static final SerializedSubject<Boolean, Boolean> initialized = new SerializedSubject<>(BehaviorSubject.create());

    @Getter(value = AccessLevel.PACKAGE)
    private final MGPreferenceData<T> metaData;

    public static void init(Context context) {

        getInitialized().onNext(true);
    }

    public static <T> MGPreference<T> create(@NonNull String key, T defaultValue, int serializationDelay) {

        return new MGPreference<>(MGPreferenceData.create(key, defaultValue, serializationDelay));
    }

    public static <T> MGPreference<T> create(@NonNull String key, T defaultValue) {

        return create(key, defaultValue, 100);
    }

    public static <T> MGPreference<T> create(@NonNull String key) {

        return create(key, null);
    }

    /**
     * Resets the entire cache for versioned data.
     */
    public static void reset() {

        getDataStore().reset();
    }

    /**
     * Public preference setter, abstracts away the
     * native commit type and allows for persisting
     * more complex types such as dictionaries.
     */
    public void set(T value) {

        metaData.set(value);
    }

    /**
     * Public preference getter.  Supports expanded commit types
     * backed by the native commit types supported by android.
     */
    public T get() {

        return metaData.get();
    }

    /**
     * Clear out the preference value from local
     * and native stores.
     */
    public void clear() {

        metaData.clear();
    }
}
