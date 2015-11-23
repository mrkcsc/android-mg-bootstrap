package com.miguelgaeta;

import com.miguelgaeta.bootstrap.mg_backgrounded.MGBackgrounded;
import com.miguelgaeta.bootstrap.mg_images.MGImagesConfig;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitions;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitionsType;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import net.danlew.android.joda.JodaTimeAndroid;

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

        JodaTimeAndroid.init(this);

        // Initialize logging.
        MGLog.getConfig().init(this);

        // Initialize reflection.
        MGReflection.getConfig().init(this);

        // Initialize preferences.
        MGPreference.init(this);

        MGImagesConfig.init(this);

        MGBackgrounded.getConfig().init(this);

        MGLifecycleActivityTransitions.setDefaultType(MGLifecycleActivityTransitionsType.SLIDE_HORIZONTAL);
    }
}
