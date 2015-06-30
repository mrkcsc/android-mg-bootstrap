package com.miguelgaeta.bootstrap.mg_preference;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.concurrent.Executors;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGPreferenceConfig {

    // Custom scheduler to keep preferences from
    // overloading the device.
    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private static final Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

    // For any libraries within core, store a context
    // to make the calling API easier but make sure
    // that it is a application context to ensure no
    // activities or fragments can be leaked.
    @Getter
    private Context context;

    // Used as a cache breaker to avoid problems
    // in version increments due to changes in preference
    // data structures or classes.
    @Getter(AccessLevel.PACKAGE)
    private int versionCode;

    /**
     * Standard initialization call.
     *
     * @param context Application context.
     */
    public void init(Context context) {

        if (context instanceof Application) {

            // Set the context.
            this.context = context;

            // Set the version code.
            this.versionCode = getVersionCode(context);

        } else {

            // Enforce use of an application context.
            throw new RuntimeException("An application context is required.");
        }
    }

    /**
     * Fetch version code used as a cache breaker.
     */
    private int getVersionCode(Context context) {

        PackageManager manager = context.getPackageManager();

        try {

            // Fetch current version code from the current package.
            return manager.getPackageInfo(context.getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            return 0;
        }
    }
}
