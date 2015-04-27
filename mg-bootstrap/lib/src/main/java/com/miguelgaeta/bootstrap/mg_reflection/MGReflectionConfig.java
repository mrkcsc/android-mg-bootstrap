package com.miguelgaeta.bootstrap.mg_reflection;

import android.app.Application;
import android.content.Context;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGReflectionConfig {

    @Getter(AccessLevel.PACKAGE)
    private Context context;

    /**
     * Initialize the reflection library
     * with an application context.
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