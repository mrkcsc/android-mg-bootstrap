package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("unchecked")
class MGPreferenceMetaData<T> {

    // Standard configuration object.
    private final MGPreferenceConfig config;

    // A memory cache of the preference value
    // so we do not have to go into the native
    // android preferences every fetch.
    private T locallyCachedValue;

    // Type token for deserialization.
    private TypeToken typeToken;

    // The key used by the android preferences
    // underlying system (should be unique).
    private final String key;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreferenceMetaData(String key, MGPreferenceConfig config) {

        // Set config.
        this.config = config;

        // Set key.
        this.key = key;
    }

    /**
     * Fetch the native android shared
     * preferences object.
     */
    private SharedPreferences getSharedPreferences() {

        return PreferenceManager.getDefaultSharedPreferences(config.getContext());
    }

    /**
     * Fetch and open the native android
     * shared preferences editor.
     */
    private SharedPreferences.Editor getSharedPreferencesEditor() {

        return getSharedPreferences().edit();
    }

    /**
     * Clear out any value stored.
     */
    void clear() {

        SharedPreferences.Editor editor = getSharedPreferencesEditor();

        editor.remove(key);
        editor.apply();

        locallyCachedValue = null;
    }

    /**
     * Get the preference value.
     */
    T get() {

        // Return cached value if present.
        if (locallyCachedValue != null) {

            return locallyCachedValue;
        }

        if (typeToken != null) {

            // Fetch persisted value from shared preferences and serialize with type token.
            return new Gson().fromJson(getSharedPreferences().getString(key, null), typeToken.getType());
        }

        return null;
    }

    /**
     * Set the preference value.
     */
    void set(@NonNull T value) {

        // Save a type token for deserialization.
        typeToken = TypeToken.get(value.getClass());

        SharedPreferences.Editor editor = getSharedPreferencesEditor();

        editor.putString(key, new Gson().toJson(value));
        editor.apply();

        // Cache value in memory.
        locallyCachedValue = value;
    }
}
