package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Map;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("unchecked")
class MGPreferenceMetaData<T> {

    private static final String TYPE_COLLECTION = "Collection";
    private static final String TYPE_MAP = "Map";

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

    // Gson serializer.
    private final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

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

        try {

            // Fetch raw token.
            String typeTokenRaw = getSharedPreferences().getString(keyTypeToken, null);

            if (typeTokenRaw != null) {

                // Fetch raw json string.
                String json = getSharedPreferences().getString(key, null);

                // Fetch type tokens array.
                String[] typeTokens = typeTokenRaw.split(",");

                if (typeTokens.length > 1) {

                    switch (typeTokens[0]) {
                        case TYPE_COLLECTION:

                            // Fetch an instance of the iterable class.
                            Collection<T> collection = (Collection<T>) Class.forName(typeTokens[1]).newInstance();

                            // Fetch the class of the collection elements.
                            Class<T> jsonArrayElementClass = (Class<T>)Class.forName(typeTokens[2]);

                            for (final JsonElement element: gson.fromJson(json, JsonArray.class)) {

                                // Serialize each element into the collection.
                                collection.add(gson.fromJson(element, jsonArrayElementClass));
                            }

                            locallyCachedValue = (T)collection;

                            break;
                        case TYPE_MAP:

                            Map<?, ?> map = (Map<?, ?>)Class.forName(typeTokens[1]).newInstance();

                            Class<?> jsonMapElementKeyClass = Class.forName(typeTokens[2]);
                            Class<?> jsonMapElementValueClass = Class.forName(typeTokens[3]);

                            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

                            if (jsonObject.isJsonArray()) {

                                for (final JsonElement entry: jsonObject.getAsJsonArray()) {

                                    JsonArray entryValues = entry.getAsJsonArray();

                                    ((Map) map).put(
                                        gson.fromJson(entryValues.get(0), jsonMapElementKeyClass),
                                        gson.fromJson(entryValues.get(1), jsonMapElementValueClass));
                                }

                            } else {

                                for (Map.Entry<String, JsonElement>  entry : jsonObject.entrySet()) {

                                    ((Map) map).put(
                                        gson.fromJson(entry.getKey(), jsonMapElementKeyClass),
                                        gson.fromJson(entry.getValue(), jsonMapElementValueClass));
                                }
                            }

                            locallyCachedValue = (T)map;

                            break;
                        default:
                            break;
                    }
                } else {

                    // Fetch persisted value and serialize with type token.
                    locallyCachedValue = gson.fromJson(json, (Class<T>) Class.forName(typeTokens[0]));
                }
            }

        } catch (Exception e) {

            // Should not happen.
            throw new RuntimeException("Deserialization error: " + e);
        }

        return locallyCachedValue;
    }

    /**
     * Set the preference value.
     */
    void set(T value) {

        if (value == null) {

            clear();

        } else {

            // Locally cache value.
            locallyCachedValue = value;

            SharedPreferences.Editor editor = getSharedPreferencesEditor();

            editor.putString(key, gson.toJson(value));
            editor.putString(keyTypeToken, getTypeToken(value));
            editor.apply();
        }
    }

    /**
     * Get type token for a given value.
     */
    private String getTypeToken(T value) {

        // By default just use normal class name.
        String typeToken = value.getClass().getName();

        if (value instanceof Collection && ((Collection)value).size() > 0) {

            // Special type token for collections and all associated implementations.
            return TYPE_COLLECTION + "," + typeToken + "," + ((Collection)value).iterator().next().getClass().getName();
        }

        if (value instanceof Map && ((Map)value).size() > 0) {

            // Fetch key from map.
            Object key = ((Map)value).keySet().iterator().next();

            // Special type token for maps and all associated implementations.
            return TYPE_MAP + "," + typeToken + "," + key.getClass().getName() + "," + ((Map)value).get(key).getClass().getName();
        }

        return typeToken;
    }
}
