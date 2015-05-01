package com.miguelgaeta.bootstrap.mg_backgrounded;

import android.app.Application;

import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 2/17/15.
 */
public class MGBackgroundedConfig {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private boolean initialized;

    @Getter(AccessLevel.PRIVATE)
    private final MGBackgroundedUtil util = new MGBackgroundedUtil();

    /**
     * On initialize start tracking background
     * state via lifecycle callbacks.
     */
    public void init(Application application) {

        getUtil().registerCallbacks(application);

        setInitialized(true);
    }

    /**
     * Fetch the backgrounded observable and also
     * ensure that the utility has been initialized.
     */
    MGPreferenceRx<Boolean> getBackgrounded() {

        if (!isInitialized()) {

            throw new RuntimeException("Backgrounded utility has not been initialized.");
        }

        return getUtil().getBackgrounded();
    }
}