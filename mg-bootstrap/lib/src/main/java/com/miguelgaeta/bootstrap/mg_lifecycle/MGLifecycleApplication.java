package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Application;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLifecycleApplication extends Application {

    /**
     * Initialize libraries that require
     * application level contexts.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize reflection.
        MGReflection.getConfig().init(this);

        // Initialize preferences.
        MGPreference.getConfig().init(this);
    }
}
