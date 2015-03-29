package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

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

    // The key used by the android preferences
    // underlying system (should be unique).
    private final String key;
    private final String keyTypeToken;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreferenceMetaData(String key, MGPreferenceConfig config) {

        // Set config.
        this.config = config;

        // Set key.
        this.key = key;
        this.keyTypeToken = key + "TYPE_TOKEN";
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

        String typeToken = getSharedPreferences().getString(keyTypeToken, null);

        if (typeToken != null) {

            try {

                // Fetch persisted value and serialize with type token.
                locallyCachedValue = new Gson().fromJson(getSharedPreferences()
                        .getString(key, null), (Class<T>)Class.forName(typeToken));

                return locallyCachedValue;

            } catch (ClassNotFoundException ignored) { }
        }

        return null;
    }

    /**
     * Set the preference value.
     */
    void set(@NonNull T value) {

        // Locally cache value.
        locallyCachedValue = value;

        SharedPreferences.Editor editor = getSharedPreferencesEditor();

        editor.putString(key, new Gson().toJson(value));
        editor.putString(keyTypeToken, value.getClass().getName());
        editor.apply();
    }
}
