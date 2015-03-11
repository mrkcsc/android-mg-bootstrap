package com.miguelgaeta;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;


public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MGDelay.delay(1000, () -> Log.e("", "lol"));
    }
}
