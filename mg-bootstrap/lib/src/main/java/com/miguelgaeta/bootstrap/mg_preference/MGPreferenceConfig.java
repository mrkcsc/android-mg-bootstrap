package com.miguelgaeta.bootstrap.mg_preference;

import android.app.Application;
import android.content.Context;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/9/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGPreferenceConfig {

    // For any libraries within core, store a context
    // to make the calling API easier but make sure
    // that it is a application context to ensure no
    // activities or fragments can be leaked.
    @Getter
    private Context context;

    /**
     * Standard initialization call.
     *
     * @param context Application context.
     */
    public void init(Context context) {

        if (context instanceof Application) {

            // Set the context.
            this.context = context;

        } else {

            // Enforce use of an application context.
            throw new RuntimeException("An application context is required.");
        }
    }
}
