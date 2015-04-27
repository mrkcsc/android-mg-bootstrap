package com.miguelgaeta.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import lombok.NonNull;
import rx.Observable;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerAdapter extends MGRecyclerAdapter {

    public TestActivityRecyclerAdapter(@NonNull RecyclerView recycler, @NonNull Observable<Void> paused, @NonNull Observable<Void> resumed) {
        super(recycler, paused, resumed);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TestActivityRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_activity_recycler_item, parent, false), this);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
