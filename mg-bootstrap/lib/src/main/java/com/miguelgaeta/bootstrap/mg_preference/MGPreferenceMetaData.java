package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("unchecked")
class MGPreferenceMetaData<T> {

    /**
     * Commit type supported by the preference
     * class utility.
     */
    enum CommitType {
        BOOLEAN, INTEGER, STRING, STRING_MAP
    }

    /**
     * Native commit type that is used
     * by the android system.
     */
    private enum NativeCommitType {
        BOOLEAN, INTEGER, STRING
    }

    // Standard configuration object.
    private final MGPreferenceConfig config;

    // A memory cache of the preference value
    // so we do not have to go into the native
    // android preferences every fetch.
    private Object locallyCachedValue;

    // The commit type of this preference object.
    private final CommitType commitType;

    // The key used by the android preferences
    // underlying system (should be unique).
    private final String key;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreferenceMetaData(String key, CommitType commitType, MGPreferenceConfig config) {

        // Set config.
        this.config = config;

        // Set key.
        this.key = key;

        // Set commit type.
        this.commitType = commitType;
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

        switch (commitType) {
            case BOOLEAN:
                return (T)get(NativeCommitType.BOOLEAN);
            case INTEGER:
                return (T)get(NativeCommitType.INTEGER);
            case STRING:
                return (T)get(NativeCommitType.STRING);
            case STRING_MAP:

                String jsonDictionary = (String)get(NativeCommitType.STRING);

                // Convert to dictionary.
                HashMap<String, String> dictionary =
                        new Gson().fromJson(jsonDictionary, new TypeToken<HashMap<String, String>>() {}.getType());

                // Convert to dictionary and return.
                return (T)(dictionary != null ? dictionary : new HashMap<>());
        }

        return null;
    }

    /**
     * Set the preference value.
     */
    void set(T value) {

        switch (commitType) {
            case BOOLEAN:
                set(value, NativeCommitType.BOOLEAN);
                break;
            case INTEGER:
                set(value, NativeCommitType.INTEGER);
                break;
            case STRING:
                set(value, NativeCommitType.STRING);
                break;
            case STRING_MAP:

                String jsonValue = new Gson().toJsonTree(value).toString();

                set(jsonValue, NativeCommitType.STRING);
                break;
        }
    }

    /**
     * Get an object from the native android preferences
     * manager, keyed by a native commit class type.
     */
    private Object get(NativeCommitType commitType) {

        if (locallyCachedValue != null) {

            return locallyCachedValue;
        }

        switch (commitType) {
            case BOOLEAN:
                return getSharedPreferences().getBoolean(key, false);
            case INTEGER:
                return getSharedPreferences().getInt(key, 0);
            case STRING:
                return getSharedPreferences().getString(key, null);
        }

        return null;
    }

    /**
     * Persists an object into the android shared
     * preferences, also clears locally cached value.
     */
    private void set(Object value, NativeCommitType commitType) {

        SharedPreferences.Editor editor = getSharedPreferencesEditor();

        switch (commitType) {
            case BOOLEAN:
                editor.putBoolean(key, (Boolean)value);
                break;
            case INTEGER:
                editor.putInt(key, (Integer)value);
                break;
            case STRING:
                editor.putString(key, (String)value);
                break;
        }

        editor.apply();

        locallyCachedValue = value;
    }
}
