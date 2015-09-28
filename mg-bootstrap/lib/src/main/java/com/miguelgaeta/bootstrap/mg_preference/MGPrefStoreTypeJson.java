package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;

import java.lang.reflect.Type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 7/8/15.
 */
@SuppressWarnings("UnusedDeclaration")
class MGPrefStoreTypeJson extends MGPreferenceStore {

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final MGPreference<Integer> versionCodeCached = MGPreference.create("VERSION_CODE", new TypeToken<Integer>() {}, 0, false);

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final Gson gson = MGRestClient.getGson();

    @Getter(value = AccessLevel.PRIVATE)
    private SharedPreferences storeGlobal;

    @Getter(value = AccessLevel.PRIVATE)
    private SharedPreferences storeVersioned;

    /**
     * Initialize the data store by first getting the global data
     * store and reading the cached version code which
     * allows us to initialize the versioned store and also
     * clear and old store if needed.
     */
    @SuppressWarnings("UnusedDeclaration")
    public MGPrefStoreTypeJson() {

        storeGlobal = getStore(getContext(), getFileName(0));

        int versionCode = getVersionCode(getContext());
        int versionCodeCached = getVersionCodeCached().get();

        getVersionCodeCached().set(versionCode);

        storeVersioned = getStore(getContext(), getFileName(versionCode));

        clearCachedStoreIfNeeded(getContext(), versionCode, versionCodeCached);
    }

    @Override
    public Object get(@NonNull String key, Type typeOfObject, boolean versioned) {

        return getGson().fromJson(getStore(versioned).getString(key, null), typeOfObject);
    }

    @Override
    public void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned) {

        getStore(versioned).edit().putString(key, serializeValue(value, typeOfObject)).apply();
    }

    @Override
    public void clear() {

        storeVersioned.edit().clear().apply();
    }

    private String serializeValue(Object value, Type typeOfObject) throws OutOfMemoryError {

        return value == null ? null : getGson().toJson(value, typeOfObject);
    }

    /**
     * Get a shared preferences file that is either
     * global or versioned by application code.
     */
    private SharedPreferences getStore(boolean versioned) {

        return versioned ? getStoreVersioned() : getStoreGlobal();
    }

    /**
     * If we detect that a version code has changed.  Take the old
     * versioned store and clear it out.  Update the cached
     * version code.
     */
    private static void clearCachedStoreIfNeeded(@NonNull Context context, int versionCode, int versionCodeCached) {

        if (versionCodeCached != versionCode) {

            SharedPreferences storeVersioned = getStore(context, getFileName(versionCodeCached));

            storeVersioned.edit().clear().apply();
        }
    }

    /**
     * Fetch store which is represented as a private shared
     * preferences file.
     */
    private static SharedPreferences getStore(@NonNull Context context, @NonNull String fileName) {

        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
}
