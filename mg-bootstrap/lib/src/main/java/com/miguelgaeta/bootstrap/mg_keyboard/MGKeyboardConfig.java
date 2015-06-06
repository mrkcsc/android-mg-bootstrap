package com.miguelgaeta.bootstrap.mg_keyboard;

import android.app.Application;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 3/27/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGKeyboardConfig {

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private Integer rootViewResourceId;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private boolean rootViewResize;

    public void init(Application application) {

        // Register the activity callbacks.
        application.registerActivityLifecycleCallbacks(new MGKeyboardLifecycl2());
    }
}
