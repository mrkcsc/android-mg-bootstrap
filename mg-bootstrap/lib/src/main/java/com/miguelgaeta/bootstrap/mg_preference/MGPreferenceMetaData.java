package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.google.gson.Gson;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    // Default value of this item.
    private T defaultValue;

    // The key used by the android preferences
    // underlying system (should be unique).
    private final String key;
    private final String keyTypeToken;

    // Gson serializer.
    private final Gson gson = MGRestClient.getGson();

    // Holds any active serialization subscription.
    private Subscription serializationSubscription;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreferenceMetaData(String key, T defaultValue, MGPreferenceConfig config) {

        // Set config.
        this.config = config;

        // Append cache breaker.
        key += "_" + config.getVersionCode();

        // Set key.
        this.key = key;
        this.keyTypeToken = key + "_TYPE_TOKEN";

        // Set default value.
        this.defaultValue = defaultValue;
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

        if (locallyCachedValue == null) {

            // First fetch complex type token for preference.
            String typeTokenJson = getSharedPreferences().getString(keyTypeToken, null);

            if (typeTokenJson != null) {

                // Serialize type token into object.
                MGPreferenceTypeToken typeToken = MGPreferenceTypeToken.createFromJson(gson, typeTokenJson);

                // Fetch raw json associated with this preference key.
                String keyJson = getSharedPreferences().getString(key, null);

                if (keyJson != null) {

                    // Deserialize with type token and update local cache.
                    locallyCachedValue = (T)typeToken.fromJson(gson, keyJson);
                }
            }
        }

        if (locallyCachedValue == null && defaultValue != null) {

            return defaultValue;
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

            if (serializationSubscription != null) {
                serializationSubscription.unsubscribe();
            }

            // Create an observable that serializes the value and generates its type token.
            Observable<Pair<String, String>> serializationObservable = Observable.create(subscriber -> {

                String key = gson.toJson(value);
                String keyTypeToken = gson.toJson(MGPreferenceTypeToken.create(value));

                subscriber.onNext(new Pair<>(key, keyTypeToken));
                subscriber.onCompleted();
            });

            serializationSubscription = serializationObservable

                // Perform serialization on worker thread.
                .subscribeOn(Schedulers.computation())

                // Update shared preferences on main thread.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(keyTypeTokenPair -> {

                    SharedPreferences.Editor editor = getSharedPreferencesEditor();

                    editor.putString(key, keyTypeTokenPair.first);
                    editor.putString(keyTypeToken, keyTypeTokenPair.second);
                    editor.apply();
                });
        }
    }
}
