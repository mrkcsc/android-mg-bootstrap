package com.miguelgaeta.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecycler extends MGLifecycleActivity {

    @Getter
    public static final MGPreferenceRx<String> testStream = MGPreferenceRx.create("TEST_STREAM");

    @InjectView(R.id.recycler_view) RecyclerView recyclerView;

    private TestActivityRecyclerAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Recycler");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new TestActivityRecyclerAdapter(recyclerView, getPaused(), getResumed());
        recyclerView.setAdapter(recyclerViewAdapter);

        getTestStream().get(false).takeUntil(getPaused()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {

            MGLog.e("Reload");

            recyclerViewAdapter.notifyDataSetChanged();
        });
    }

    @OnClick(R.id.recycler_reload)
    public void onReloadClicked() {

        getTestStream().set("reload string");
    }
}
