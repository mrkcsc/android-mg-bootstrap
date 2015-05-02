package com.miguelgaeta.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitions;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecycler extends MGLifecycleActivity {

    @InjectView(R.id.recycler_view) RecyclerView recyclerView;

    @Getter
    public static final MGPreferenceRx<List<Integer>> testStream = MGPreferenceRx.create(null, new ArrayList<>());

    private TestActivityRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTransitions().setType(MGLifecycleActivityTransitions.Type.SLIDE_HORIZONTAL);

        getSupportActionBar().setTitle("Recycler");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestActivityRecyclerAdapter(recyclerView);
        recyclerView.setAdapter(adapter);

        List<Integer> dataInitial = new ArrayList<>();

        dataInitial.add(1);
        dataInitial.add(2);
        dataInitial.add(3);
        dataInitial.add(4);
        dataInitial.add(5);
        dataInitial.add(6);
        dataInitial.add(7);
        dataInitial.add(8);

        adapter.getData().setData(dataInitial);
    }

    @OnClick(R.id.recycler_reload)
    public void onReloadClicked() {

        List<Integer> dataNew = new ArrayList<>();

        dataNew.add(9);
        dataNew.add(10);
        dataNew.add(11);
        dataNew.add(12);
        dataNew.add(13);
        dataNew.add(14);
        dataNew.add(15);
        dataNew.add(16);

        adapter.getData().setData(dataNew);
    }
}
