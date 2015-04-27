package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 4/21/15.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class MGRecyclerAdapter extends RecyclerView.Adapter<MGRecyclerViewHolder> {

    @Getter(AccessLevel.PROTECTED)
    private final RecyclerView recycler;

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.
    */
    public MGRecyclerAdapter(@NonNull RecyclerView recycler) {

        // Set recycler view.
        this.recycler = recycler;
    }

    /**
     * Automatically configure the view holder
     * with the specified item at position.
     */
    @Override
    public void onBindViewHolder(MGRecyclerViewHolder holder, int position) {

        // Configure.
        holder.onConfigure(position);
    }
}
