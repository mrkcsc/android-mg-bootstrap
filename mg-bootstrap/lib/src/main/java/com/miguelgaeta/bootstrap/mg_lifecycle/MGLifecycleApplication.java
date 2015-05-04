package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Application;
import android.content.Context;

import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
public class MGLifecycleApplication extends Application {

    @Getter
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
