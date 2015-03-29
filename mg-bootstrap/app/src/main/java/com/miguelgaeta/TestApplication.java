package com.miguelgaeta;

import android.app.Application;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

/**
 * Created by mrkcsc on 3/28/15.
 */
public class TestApplication extends Application {

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