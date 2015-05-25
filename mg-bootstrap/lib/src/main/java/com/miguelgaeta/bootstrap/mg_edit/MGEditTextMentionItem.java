package com.miguelgaeta.bootstrap.mg_edit;

import android.support.annotation.LayoutRes;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

/**
 * Created by mrkcsc on 5/24/15.
 */
public abstract class MGEditTextMentionItem extends MGRecyclerViewHolder<MGEditTextMentionAdapter> {

    public MGEditTextMentionItem(@LayoutRes int layout, MGEditTextMentionAdapter adapter) {
        super(layout, adapter);
    }

    @Override
    protected void onConfigure(int position) {
        super.onConfigure(position);
    }

    public abstract void a();
}
