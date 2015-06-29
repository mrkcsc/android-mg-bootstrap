package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by mrkcsc on 12/5/14.
 */
@SuppressWarnings("unchecked, unused")
public class MGPreference<T> {

    @Getter(lazy = true)
    private static final MGPreferenceConfig config = new MGPreferenceConfig();

    // Meta data object to do the persisting.
    private MGPreferenceMetaData<T> metaData;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreference(String key, TypeToken<?> typeToken, T defaultValue, boolean cacheBreaker) {

        metaData = new MGPreferenceMetaData<>(key, typeToken, defaultValue, cacheBreaker);
    }

    public static <T> MGPreference<T> create(@NonNull String key, @NonNull TypeToken<?> typeToken, T defaultValue, boolean cacheBreaker) {

        return new MGPreference<>(key, typeToken, defaultValue, cacheBreaker);
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

        metaData.clear();
    }
}
