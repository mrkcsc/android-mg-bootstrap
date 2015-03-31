package com.miguelgaeta;

import android.os.Bundle;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_anim.MGAnimFade;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_websocket.MGWebsocket;

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

        MGLog.e("LOl test");

        MGWebsocket websocket =  MGWebsocket.create();

        websocket.getConfig().setUrl("ws://echo.websocket.org");
        websocket.getConfig().setReconnect(true);
        websocket.getConfig().setBuffered(true);

        websocket.getOnOpen()
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(getPaused())
                .subscribe(open -> {

                    MGLog.e("Open: " + open);
                });

        websocket.getOnClose()
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(getPaused())
                .subscribe(closed -> {

                    MGLog.e("Closed: " + closed);
                });

        websocket.getOnError()
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(getPaused())
                .subscribe(error -> {

                    MGLog.e("Error: " + error);
                });

        websocket.getOnMessage()
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeUntil(getPaused())
                .subscribe(message -> {

                    MGLog.e("Message: " + message);
                });

        websocket.connect();
        websocket.message("Test message");

        getPaused().subscribe(o -> {

            websocket.close();
        });
    }

    @OnClick(R.id.test_button)
    public void testButtonClick() {
        startActivity(TestActivityNext.class);
    }
}
