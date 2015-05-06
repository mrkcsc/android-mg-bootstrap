package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 5/6/15.
 */
@SuppressWarnings("unused")
public abstract class MGRecyclerAdapterSimple extends MGRecyclerAdapter {

    // Data list that powers the adapter.
    private MGRecyclerDataList<MGRecyclerDataPayload.Item> data = MGRecyclerDataList.create(this, new ArrayList<>(), MGRecyclerDataPayload.Item::getKey);

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.
     */
    public MGRecyclerAdapterSimple(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public int getItemViewType(int position) {

        return data.get().get(position).getType();
    }

    @Override
    public int getItemCount() {

        return data.get().size();
    }

    /**
     * Get data item object at specified position.
     */
    public Object getItem(int position) {

        return data.get().get(position).getItem();
    }

    /**
     * Provide a new data set to the adapter.  Will take
     * care of updating the data set automatically.
     */
    public void setData(List<MGRecyclerDataPayload.Item> data) {

        this.data.set(data);
    }
}
