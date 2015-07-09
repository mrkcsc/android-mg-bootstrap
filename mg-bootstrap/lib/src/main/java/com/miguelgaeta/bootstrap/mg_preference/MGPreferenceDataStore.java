package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.google.gson.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 7/8/15.
 */
class MGPreferenceDataStore {

    private static final String DATA_PREFIX = "PREFERENCE_";

    @Getter(value = AccessLevel.PRIVATE, lazy = true)
    private final MGPreference<Integer> versionCodeCached = MGPreference.create("VERSION_CODE", new TypeToken<Integer>() {}, 0, false);

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
    public void init(@NonNull Context context) {

        storeGlobal = getStore(context, DATA_PREFIX + "SHARED");

        int versionCode = getVersionCode(context);
        int versionCodeCached = getVersionCodeCached().get();

        getVersionCodeCached().set(versionCode);

        storeVersioned = getStore(context, getVersionedStoreName(versionCode));

        clearCachedStoreIfNeeded(context, versionCode, versionCodeCached);
    }

    public String get(@NonNull String key, boolean versioned) {

        return getStore(versioned).getString(key, null);
    }

    public void set(@NonNull String key, String value, boolean versioned) {

        getStore(versioned).edit().putString(key, value).apply();
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

            SharedPreferences storeVersioned = getStore(context, getVersionedStoreName(versionCodeCached));

            storeVersioned.edit().clear().apply();
        }
    }

    /**
     * Attempts to extract the version code of the
     * host application.
     */
    private static int getVersionCode(@NonNull Context context) {

        PackageManager manager = context.getPackageManager();

        try {

            return manager.getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            return 0;
        }
    }

    /**
     * Fetch store which is represented as a private shared
     * preferences file.
     */
    private static SharedPreferences getStore(@NonNull Context context, @NonNull String fileName) {

        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * Use a naming scheme to version each store file as
     * the application changes.
     */
    private static String getVersionedStoreName(int versionCode) {

        return DATA_PREFIX + "VERSION_" + versionCode;
    }
}