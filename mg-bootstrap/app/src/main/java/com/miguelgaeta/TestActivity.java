package com.miguelgaeta;

import android.os.Bundle;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_anim.MGAnimFade;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClientErrorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import rx.android.schedulers.AndroidSchedulers;


public class TestActivity extends MGLifecycleActivity {

    @Data
    private static class TestPref {

        private List<MGRestClientErrorModel> payload = new ArrayList<>();
    }

    @Data @AllArgsConstructor
    private static class TestData {

        private int data1;
        private String data2;
    }

    @Getter(lazy = true)
    private static final MGPreference<Map<Integer, List<TestData>>> pref = MGPreference.create("TEST_PREF_10");

    //@Getter(lazy = true)
    //private static final MGPreferenceRx<TestPref> pref1 = MGPreferenceRx.create("TEST_PREF_3");

    @InjectView(R.id.fade_test_view) View fadeTestView;

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

        getPref().set(prefData);

        for (Integer integer: getPref().get().keySet()) {

            MGLog.e("Key: " + integer + " value: " + getPref().get().get(integer));
        }

        //getPref1().get().subscribe(testPref -> {

        //    MGLog.e("Pref: " + testPref);
        //});

        /*
        getPref().get()
                .takeUntil(getPaused())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(testPref -> {

                    MGLog.e("Value: " + testPref);
                });

        MGDelay.delay(1000)
                .takeUntil(getPaused())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (getPref().getBlocking() == null) {
                        getPref().set(new TestPref());

                        MGLog.e("New pref");
                    } else {

                        TestPref testPref = getPref().getBlocking();

                        testPref.getPayload().add("new value");

                        MGLog.e("Pushing: " + testPref);

                        getPref().set(testPref);
                    }
                });
                */

        fadeTestView.setVisibility(View.GONE);

        MGDelay.delay(1000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {

                    MGAnimFade.setVisibility(fadeTestView, View.VISIBLE);
                });

        /*
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
        */
    }

    protected void onCreateOrResume() {
        super.onCreateOrResume();

        MGDelay.delay(5000).observeOn(AndroidSchedulers.mainThread()).subscribe(aVoid -> {

            startActivity(TestActivityNext.class);
        });
    }

    @OnClick(R.id.test_button)
    public void testButtonClick() {
        startActivity(TestActivityNext.class);
    }
}
