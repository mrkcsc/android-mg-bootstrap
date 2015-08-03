package com.miguelgaeta.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerDataList;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import java.util.ArrayList;

import lombok.Getter;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerAdapter extends MGRecyclerAdapter {

    @Getter
    private MGRecyclerDataList<Integer> data = MGRecyclerDataList.create(this, new ArrayList<>(), integer -> Integer.toString(integer));

    public TestActivityRecyclerAdapter(RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TestActivityRecyclerViewHolder(R.layout.test_activity_recycler_item, this);
    }

    @Override
    public int getItemCount() {

        return data.get().size();
    }
}
