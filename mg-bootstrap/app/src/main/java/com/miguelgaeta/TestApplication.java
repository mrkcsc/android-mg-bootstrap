package com.miguelgaeta;

import com.miguelgaeta.bootstrap.mg_images.MGImagesConfig;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitions;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitionsType;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

/**
 * Created by mrkcsc on 3/28/15.
 */
public class TestApplication extends MGLifecycleApplication {

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
        MGPreference.init(this);

        MGImagesConfig.init(this);

        MGLifecycleActivityTransitions.setDefaultType(MGLifecycleActivityTransitionsType.SLIDE_HORIZONTAL);
    }
}
