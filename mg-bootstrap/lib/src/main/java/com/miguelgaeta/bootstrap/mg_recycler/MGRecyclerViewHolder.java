package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGRecyclerViewHolder<T extends MGRecyclerAdapter> extends RecyclerView.ViewHolder {

    @Getter(AccessLevel.PROTECTED)
    private final T adapter;

    /**
     * This recycler view holder subclass exposes
     * the associated adapter so it can self
     * configure itself.
     */
    public MGRecyclerViewHolder(View itemView, T adapter) {
        super(itemView);

        // Set the adapter.
        this.adapter = adapter;

        // Enable butter knife.
        ButterKnife.inject(this, itemView);
    }

    /**
     * Short hand that auto inflated the resource.
     */
    public MGRecyclerViewHolder(@LayoutRes int layout, T adapter) {

        this(LayoutInflater.from(adapter.getRecycler().getContext()).inflate(layout, adapter.getRecycler(), false), adapter);
    }

    /**
     * Simulated the one create method
     * of fragments and activities.
     */
    protected void onConfigure(int position) {

    }
}
