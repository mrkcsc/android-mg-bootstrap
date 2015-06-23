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
        Timber.plant(debug ? new Timber.DebugTree() : new ProductionTree());
    }

    private class ProductionTree extends Timber.Tree {

        @Override
        public void i(String message, Object... args) {

            log(info, null, message, args);
        }

        @Override
        public void i(Throwable t, String message, Object... args) {

            log(info, t, message, args);
        }

        @Override
        public void e(String message, Object... args) {

            log(error, null, message, args);
        }

        @Override
        public void e(Throwable t, String message, Object... args) {

            log(error, t, message, args);
        }

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

            // Timber has weird behavior for constructing the message string
            // so in production bypass this entirely and use custom logic.
        }

        private void log(MGLog.Callback callback, Throwable throwable, String message, Object... args) {

            if (message == null) {
                message = "";
            }

            if (callback != null) {
                callback.run(throwable, args.length > 0 ? String.format(message, args) : message);
            }
        }
    }
}
