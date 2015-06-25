package com.miguelgaeta.recycler;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivity;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleActivityTransitionsType;
import com.miguelgaeta.bootstrap.mg_preference.MGPreferenceRx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        getTransitions().setType(MGLifecycleActivityTransitionsType.SLIDE_HORIZONTAL);

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

        adapter.getData().set(dataInitial);
    }

    @OnClick(R.id.recycler_reload)
    public void onReloadClicked() {

        List<Integer> dataNew = new ArrayList<>();

        int size = randInt(1, 10);

        for (int i = 0; i < size; i++) {

            int randValue;

            do {

                randValue = randInt(1, 15);

            } while(dataNew.contains(randValue));

            dataNew.add(randValue);
        }

        adapter.getData().set(dataNew);
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }
}
