package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rx.Scheduler;
import rx.functions.Action1;
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

    @Getter(value = AccessLevel.PACKAGE, lazy = true)
    private static final List<WeakReference<MGPreference>> preferences = new ArrayList<>();

    @Getter @Setter
    private static Action1<Error> errorHandler = error -> {

    };

    @Getter(value = AccessLevel.PACKAGE)
    private final MGPreferenceData<T> metaData;

    public static void init(Context context) {

        getInitialized().onNext(true);
    }

    public static <T> MGPreference<T> create(@NonNull String key, T defaultValue, int serializationDelay) {

        final MGPreference<T> preference = new MGPreference<>(MGPreferenceData.create(key, defaultValue, serializationDelay));

        getPreferences().add(new WeakReference<>(preference));

        return preference;
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

        // Reset any preferences in memory.
        for (WeakReference<MGPreference> weakPreferenceReference : getPreferences()) {

            if (weakPreferenceReference.get() != null) {
                weakPreferenceReference.get().clear();
            }
        }

        // Hard delete files.
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

    @RequiredArgsConstructor
    public static class Error {
        public final String name;
        public final Throwable exception;
    }
}
