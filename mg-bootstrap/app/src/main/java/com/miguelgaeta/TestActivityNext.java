package com.miguelgaeta;

import android.os.Bundle;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitions;

/**
 * Created by Miguel Gaeta on 3/23/15.
 */
public class TestActivityNext extends MGLifecycleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTransitions().setType(MGLifecycleActivityTransitions.Type.NONE);
    }
}
