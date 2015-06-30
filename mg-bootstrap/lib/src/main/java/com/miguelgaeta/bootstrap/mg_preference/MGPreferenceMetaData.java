package com.miguelgaeta.bootstrap.mg_preference;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.NonNull;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("unchecked")
class MGPreferenceMetaData<T> {

    // A memory cache of the preference value
    // so we do not have to go into the native
    // android preferences every fetch.
    private T locallyCachedValue;

    // Default value of this item.
    private T defaultValue;

    // The key used by the android preferences
    // underlying system (should be unique).
    private final String key;

    // Gson serializer.
    private final Gson gson = MGRestClient.getGson();

    // Holds any active serialization subscription.
    private Subscription serializationSubscription;

    private TypeToken<?> typeToken;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    MGPreferenceMetaData(String key, @NonNull TypeToken<?> typeToken, T defaultValue, boolean cacheBreaker) {

        if (cacheBreaker) {

            // Append cache breaker.
            key += "_" + MGPreference.getConfig().getVersionCode();
        }

        this.key = key;
        this.typeToken = typeToken;
        this.defaultValue = defaultValue;
    }

    /**
     * Fetch the native android shared
     * preferences object.
     */
    private SharedPreferences getSharedPreferences() {

        return PreferenceManager.getDefaultSharedPreferences(MGPreference.getConfig().getContext());
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

            String valueJson = getSharedPreferences().getString(key, null);

            if (valueJson != null) {

                locallyCachedValue = gson.fromJson(valueJson, typeToken.getType());
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
            Observable<String> serializationObservable = Observable.create(subscriber -> {

                String valueJson = gson.toJson(value, typeToken.getType());

                subscriber.onNext(valueJson);
                subscriber.onCompleted();
            });

            serializationSubscription = serializationObservable

                .subscribeOn(MGPreferenceConfig.getScheduler())

                // Update shared preferences on main thread.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valueJson -> {

                    SharedPreferences.Editor editor = getSharedPreferencesEditor();

                    editor.putString(key, valueJson);
                    editor.apply();

                }, MGRxError.create(null, "Unable to serialize preference."));
        }
    }
}
