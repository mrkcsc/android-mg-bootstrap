package com.miguelgaeta.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import lombok.NonNull;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerAdapter extends MGRecyclerAdapter {

    public TestActivityRecyclerAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TestActivityRecyclerViewHolder(R.layout.test_activity_recycler_item, this);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
