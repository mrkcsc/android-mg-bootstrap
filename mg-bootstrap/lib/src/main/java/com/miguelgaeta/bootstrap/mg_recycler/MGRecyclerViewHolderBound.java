package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.annotation.LayoutRes;
import android.view.View;

import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGRecyclerViewHolderBound<T extends MGRecyclerAdapter, D> extends MGRecyclerViewHolder<T> {

    private final Func1<Integer, D> getDataCallback;

    public MGRecyclerViewHolderBound(View itemView, T adapter, Func1<Integer, D> getDataCallback) {
        super(itemView, adapter);

        this.getDataCallback = getDataCallback;
    }

    public MGRecyclerViewHolderBound(@LayoutRes int layout, T adapter, Func1<Integer, D> getDataCallback) {
        super(layout, adapter);

        this.getDataCallback = getDataCallback;
    }

    public D getData(int position) {

        return getDataCallback.call(position);
    }
}
