package com.miguelgaeta.bootstrap.mg_reflection;

import android.content.Context;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGReflectionConfig {

    Context context;

    /**
     * Initialize the reflection library
     * with an application context.
     */
    public void init(Context context) {

        this.context = context;
    }
}