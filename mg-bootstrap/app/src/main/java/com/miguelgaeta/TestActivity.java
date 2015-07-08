package com.miguelgaeta;

import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_anim.MGAnimFade;
import com.miguelgaeta.bootstrap.mg_delay.MGDelay;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_preference.MGPreference;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClient;
import com.miguelgaeta.bootstrap.mg_rest.MGRestClientErrorModel;
import com.miguelgaeta.bootstrap.mg_websocket.MGWebsocket;

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
    private static final MGPreference<Map<Integer, List<TestData>>> pref = MGPreference.create("TEST_PREF_10", new TypeToken<Map<Integer, List<TestData>>>() {}, new HashMap<>());

    @InjectView(R.id.fade_test_view) View fadeTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MGLog.e("Wut: ");

        String json = null;

        Boolean result = MGRestClient.getGson().fromJson(json, Boolean.class);

        MGLog.e("Serializing null json: " + result);

        MGLog.e("Obj: " + getPref().get());

        Map<Integer, List<TestData>> prefData = new HashMap<>();

        List<TestData> td1 = new ArrayList<>();
        List<TestData> td2 = new ArrayList<>();

        td1.add(new TestData(10, "td1_1"));
        td1.add(new TestData(11, "td1_2"));

        td2.add(new TestData(20, "td2_1"));
        td2.add(new TestData(21, "td2_2"));

        prefData.put(-10, td1);
        prefData.put(-11, td2);

        // Test shit data.
        prefData.put(-12, new ArrayList<>());

        getPref().set(prefData);

        MGLog.e("Obj: " + getPref().get());

        /*
        MGImages.getBitmap("http://i1-news.softpedia-static.com/images/news2/Facebook-Messenger-for-Android-Updated-with-Ability-to-Save-Videos-to-Phone-s-Gallery-449351-3.jpg")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(bitmap -> {

                Toast.makeText(this, bitmap == null ? R.string.shared_lorum_ipsum_tiny : R.string.shared_number_9999, Toast.LENGTH_SHORT).show();

                if (bitmap != null) {

                    // Download image to gallery.
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "test");
                }

            }, MGRxError.create(null, "lol what"));
            */

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



    }

    protected void onCreateOrResume() {
        super.onCreateOrResume();

        //setupSocket();
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

            MGLog.e("Open: " + open);
        });

        websocket.onClosed().takeUntil(getPaused()).subscribe(closed -> {

            MGLog.e("Closed: " + closed);
        });

        websocket.onError().takeUntil(getPaused()).subscribe(error -> {

            MGLog.e("Error: " + error);
        });

        websocket.onMessage().takeUntil(getPaused()).subscribe(message -> {

            MGLog.e("Message: " + message);
        });

        websocket.connect();
        websocket.message("{\"cursor\":-1,\"channel\":\"add-topic-to-channel_fantasy-football\",\"action\":\"subscribe\"}");
        websocket.heartBeat(10000, "ping");

        getPaused().subscribe(o -> {

            websocket.disconnect();
        });
    }
}
