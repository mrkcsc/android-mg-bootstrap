package com.miguelgaeta.bootstrap.mg_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MGViewRecyclerHolder<T extends RecyclerView.Adapter> extends RecyclerView.ViewHolder {

    @Getter
    private T adapter;

    @Getter
    private View itemView;

    private MGViewRecyclerHolder(View itemView) {
        super(itemView);
    }

    public MGViewRecyclerHolder(View itemView, T adapter) {
        super(itemView);

        // Set the adapter.
        this.adapter = adapter;

        // Set the item view.
        this.itemView = itemView;

        // Enable butter knife.
        ButterKnife.inject(this, itemView);
    }
}
