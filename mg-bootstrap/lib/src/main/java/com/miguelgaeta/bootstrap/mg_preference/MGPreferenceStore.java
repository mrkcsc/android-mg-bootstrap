package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;
import android.content.pm.PackageManager;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
abstract class MGPreferenceStore implements MGPrefStoreInterface {

    private static final String DATA_PREFIX = "PREF";

    /**
     * Attempts to extract the version code of the
     * host application.
     */
    static int getVersionCode(@NonNull Context context) {

        PackageManager manager = context.getPackageManager();

        try {

            return manager.getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            return 0;
        }
    }

    /**
     * Use a naming scheme to version each store file as
     * the application changes.
     */
    static String getFileName(int versionCode) {

        return DATA_PREFIX + "_V_" + versionCode;
    }

    static String getFileName(int versionCode, String key) {

        return DATA_PREFIX + "_V_" + versionCode + "_K_" + key;
    }
}
