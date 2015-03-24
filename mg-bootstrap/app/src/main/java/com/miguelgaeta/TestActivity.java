package com.miguelgaeta;

import android.os.Bundle;
import android.util.Log;

import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import butterknife.OnClick;


public class TestActivity extends MGLifecycleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MGDelay.delay(1000, () -> Log.e("", "lol"));

        MGLog.getConfig().init(this);

        MGLog.getConfig().setError((t, message, args) -> {

        });
        MGLog.getConfig().setInfo((t, message, args) -> {

        });

        MGLog.e("LOl test");
    }

    @OnClick(R.id.test_button)
    public void testButtonClick() {
        startActivity(TestActivityNext.class);
    }
}
