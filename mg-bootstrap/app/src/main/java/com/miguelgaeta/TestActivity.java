package com.miguelgaeta;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_log.MGLog;


public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MGDelay.delay(1000, () -> Log.e("", "lol"));

        MGLog.getConfig().init(this);

        MGLog.getConfig().setError((t, message, args) -> {

        });
        MGLog.getConfig().setInfo((t, message, args) -> {

        });


        MGLog.e("LOl test");
    }
}
