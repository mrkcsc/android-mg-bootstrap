package com.miguelgaeta;

import android.os.Bundle;
import android.view.View;

import com.miguelgaeta.bootstrap.keyboarder.Keyboarder;
import com.miguelgaeta.bootstrap.mg_anim.MGAnimFade;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_websocket.MGWebsocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import rx.android.schedulers.AndroidSchedulers;


public class TestActivity extends MGLifecycleActivity {

    @Data
    private static class TestPref {

        private List<Integer> payload = new ArrayList<>();
    }

    @Data @AllArgsConstructor
    private static class TestData {

        private int data1;
        private String data2;
    }

    @Getter(lazy = true)
    private static final MGPreference<Map<Integer, List<TestData>>> pref = MGPreference.create("TEST_PREF_10", new HashMap<>());

    @Bind(R.id.fade_test_view) View fadeTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Map<Integer, List<TestData>> prefData = new HashMap<>();

        List<TestData> td1 = new ArrayList<>();
        List<TestData> td2 = new ArrayList<>();

        td1.add(new TestData(10, "td1_1"));
        td1.add(new TestData(11, "td1_2"));

        td2.add(new TestData(20, "td2_1"));
        td2.add(new TestData(21, "td2_2"));

        prefData.put(-10, td1);
        prefData.put(-11, td2);

        fadeTestView.setVisibility(View.GONE);

        MGDelay.delay(1000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                    MGAnimFade.setVisibility(fadeTestView, View.VISIBLE);
                });
    }

    protected void onCreateOrResume() {
        super.onCreateOrResume();

        final Keyboarder keyboarder = new Keyboarder(this);

        MGDelay.delay(5000).subscribe(r -> {

            keyboarder.getKeyboard().close();
        });

        getPaused().subscribe(r -> {

            keyboarder.destroy();
        });
    }

    @OnClick(R.id.test_button)
    public void testButtonClick() {
        startActivity(TestActivityNext.class);
    }

    private void setupSocket() {

        MGWebsocket websocket = new MGWebsocket();

        websocket.getConfig().setUrl("ws://echo.websocket.org");//("wss://blitzdev.net/comet/u/913c1994-a5f9-42c7-be6b-a68a5a44ec44");
        websocket.getConfig().setBuffered(true);

        websocket.onOpened().takeUntil(getPaused()).subscribe(open -> {

        });

        websocket.onClosed().takeUntil(getPaused()).subscribe(closed -> {

        });

        websocket.onError().takeUntil(getPaused()).subscribe(error -> {

        });

        websocket.onMessage().takeUntil(getPaused()).subscribe(message -> {

        });

        websocket.connect();
        websocket.message("{\"cursor\":-1,\"channel\":\"add-topic-to-channel_fantasy-football\",\"action\":\"subscribe\"}");
        websocket.heartBeat(10000, "ping");

        getPaused().subscribe(o -> {

            websocket.disconnect();
        });
    }
}
