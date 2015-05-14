package com.miguelgaeta.bootstrap.mg_log;

import android.content.Context;
import android.util.Log;

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
        Timber.plant(debug ? new Timber.DebugTree() : new ProductionTree());
    }

    /**
     * Hollow logging tree that invokes configured
     * callbacks if provided by the user.
     */
    private class ProductionTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

            if (info != null && priority == Log.INFO) {
                info.run(t, message);
            }

            if (error != null && priority == Log.ERROR) {
                error.run(t, message);
            }
        }
    }
}
