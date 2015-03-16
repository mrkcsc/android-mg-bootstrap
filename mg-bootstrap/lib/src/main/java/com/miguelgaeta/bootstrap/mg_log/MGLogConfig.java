package com.miguelgaeta.bootstrap.mg_log;

import android.content.Context;

import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import lombok.Setter;
import timber.log.Timber;

/**
 * Created by mrkcsc on 3/15/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLogConfig {

    @Setter
    private MGLog.Callback.Info info;

    @Setter
    private MGLog.Callback.Error error;

    /**
     * Initialize the logger.
     */
    public void init(Context context) {

        // Fetch debug value from current application build config.
        boolean debug = (boolean)MGReflection.getBuildConfigValue(context, "DEBUG");

        // Plant the associated tree based on environment.
        Timber.plant(debug ? new Timber.DebugTree() : getProductionTree());
    }

    /**
     * Hollow logging tree that invokes configured
     * callbacks if provided by the user.
     */
    private Timber.HollowTree getProductionTree() {

        return new Timber.HollowTree() {

            @Override
            public void i(String message, Object... args) {

                i(null, message, args);
            }

            @Override
            public void i(Throwable t, String message, Object... args) {

                if (info != null) {
                    info.run(t, message, args);
                }
            }

            @Override
            public void e(String message, Object... args) {

                e(null, message, args);
            }

            @Override
            public void e(Throwable t, String message, Object... args) {

                if (error != null) {
                    error.run(t, message, args);
                }
            }
        };
    }
}
