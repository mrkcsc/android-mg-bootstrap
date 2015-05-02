package com.miguelgaeta.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterData;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerAdapter extends MGRecyclerAdapter {

    @Getter
    private MGRecyclerAdapterData<List<Integer>> data = MGRecyclerAdapterData.create(new ArrayList<>(), (oldData, newData) -> {

    });

    public TestActivityRecyclerAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TestActivityRecyclerViewHolder(R.layout.test_activity_recycler_item, this);
    }

    @Override
    public int getItemCount() {

        return data.getData().size();
    }
}
