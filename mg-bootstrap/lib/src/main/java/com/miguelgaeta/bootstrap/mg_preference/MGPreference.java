package com.miguelgaeta.bootstrap.mg_preference;

import android.app.Application;
import android.content.Context;

import com.google.gson.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by mrkcsc on 12/5/14.
 */
@SuppressWarnings("unchecked, unused")
public class MGPreference<T> {

    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private static final MGPreferenceDataStore dataStore = new MGPreferenceDataStore();

    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private static final Scheduler scheduler = Schedulers.computation();

    // Meta data object to do the persisting.
    private MGPreferenceData<T> metaData;

    MGPreference(String key, TypeToken<?> typeToken, T defaultValue, boolean global) {

        metaData = MGPreferenceData.create(key, typeToken, defaultValue, global);
    }

    /**
     * Standard initialization call.
     *
     * @param context Application context.
     */
    public static void init(Context context) {

        if (context instanceof Application) {

            MGPreference.getDataStore().init(context);

        } else {

            // Enforce use of an application context.
            throw new RuntimeException("An application context is required.");
        }
    }

    public static <T> MGPreference<T> create(@NonNull String key, @NonNull TypeToken<?> typeToken, T defaultValue, boolean global) {

        return new MGPreference<>(key, typeToken, defaultValue, global);
    }

    public static <T> MGPreference<T> create(@NonNull String key, @NonNull TypeToken<?> typeToken, T defaultValue) {

        return create(key, typeToken, defaultValue, true);
    }

    public static <T> MGPreference<T> create(@NonNull String key, @NonNull TypeToken<?> typeToken) {

        return create(key, typeToken, null);
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

        metaData.set(null);
    }
}
