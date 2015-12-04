package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel Gaeta on 5/6/15.
 */
@SuppressWarnings("unused")
public abstract class MGRecyclerAdapterSimple extends MGRecyclerAdapter {

    // Data list that powers the adapter.
    private MGRecyclerDataList<MGRecyclerDataPayload.Contract> data = MGRecyclerDataList.create(this, new ArrayList<>(), MGRecyclerDataPayload.Contract::getKey);

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.
     */
    public MGRecyclerAdapterSimple(RecyclerView recycler) {
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
     * Add a callback action for when the
     * underlying data is updated.
     */
    public void setUpdate(MGRecyclerData.DataUpdated<List<MGRecyclerDataPayload.Contract>> updated) {

        data.setUpdated(updated);
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
    public void setData(List<MGRecyclerDataPayload.Contract> data) {

        this.data.set(data);
    }

    public void unsubscribeFromUpdates() {

        data.unsubscribeFromUpdates();
    }
}
