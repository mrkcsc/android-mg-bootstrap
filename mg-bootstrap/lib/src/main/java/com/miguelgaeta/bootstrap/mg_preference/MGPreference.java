package com.miguelgaeta.bootstrap.mg_preference;

import java.util.Map;

import lombok.Getter;

/**
 * Created by mrkcsc on 12/5/14.
 */
@SuppressWarnings("unchecked, unused")
public class MGPreference<T> {

    @Getter(lazy = true)
    private static final MGPreferenceConfig config = new MGPreferenceConfig();

    private MGPreferenceMetaData<T> metaData;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    private MGPreference(String key, MGPreferenceMetaData.CommitType commitType) {

        metaData = new MGPreferenceMetaData<>(key, commitType, getConfig());
    }

    /**
     * Create a boolean preference.
     */
    public static MGPreference<Boolean> createBoolean(String key) {

        return new MGPreference<>(key, MGPreferenceMetaData.CommitType.BOOLEAN);
    }

    /**
     * Create an integer preference.
     */
    public static MGPreference<Integer> createInteger(String key) {

        return new MGPreference<>(key, MGPreferenceMetaData.CommitType.INTEGER);
    }

    /**
     * Create a string preference.
     */
    public static MGPreference<String> createString(String key) {

        return new MGPreference<>(key, MGPreferenceMetaData.CommitType.STRING);
    }

    /**
     * Create a string map preference.
     */
    public static MGPreference<Map<String, String>> createStringMap(String key) {

        return new MGPreference<>(key, MGPreferenceMetaData.CommitType.STRING_MAP);
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
