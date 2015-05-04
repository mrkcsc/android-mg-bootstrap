package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Application;
import android.content.Context;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
public class MGLifecycleApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }

    /**
     * Fetch the application context. Should
     * be used with care.
     */
    public static Context getContext() {

        if (context == null) {

            // This should only ever happen if the consumer forgot to subclass.
            throw new RuntimeException("Application context unavailable.  Did you remember to subclass MGLifecycleApplication?");
        }

        return context;
    }
}
