package com.miguelgaeta;

import android.os.Bundle;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_anim.MGAnimFade;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;


public class TestActivity extends MGLifecycleActivity {

    @InjectView(R.id.fade_test_view) View fadeTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fadeTestView.setVisibility(View.GONE);

        MGDelay.delay(1000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                    MGAnimFade.setVisibility(fadeTestView, View.VISIBLE);
                });

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
