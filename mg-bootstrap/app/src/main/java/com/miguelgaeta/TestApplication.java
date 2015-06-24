package com.miguelgaeta;

import android.app.Application;

import com.miguelgaeta.bootstrap.mg_backgrounded.MGBackgrounded;
import com.miguelgaeta.bootstrap.mg_images.MGImagesConfig;
import com.miguelgaeta.bootstrap.mg_keyboard.MGKeyboard;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitions;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
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

        // Initialize logging.
        MGLog.getConfig().init(this);

        // Initialize reflection.
        MGReflection.getConfig().init(this);

        // Initialize preferences.
        MGPreference.getConfig().init(this);

        MGKeyboard.getConfig().init(this);
        MGKeyboard.getConfig().setRootViewResize(true);

        MGImagesConfig.init(this);

        MGBackgrounded.getConfig().init(this);

        MGLifecycleActivityTransitions.setDefaultType(MGLifecycleActivityTransitions.Type.SLIDE_HORIZONTAL);
    }
}
