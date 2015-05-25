package com.miguelgaeta.bootstrap.mg_edit;

import android.support.annotation.LayoutRes;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

/**
 * Created by mrkcsc on 5/24/15.
 */
public class MGEditTextMentionItem extends MGRecyclerViewHolder<MGEditTextMentionAdapter> {

    public MGEditTextMentionItem(@LayoutRes int layout, MGEditTextMentionAdapter adapter) {
        super(layout, adapter);

        // Handle clicks.
        itemView.setOnClickListener(view -> getAdapter().mentionClicked(getAdapterPosition()));
    }

    @Override
    protected void onConfigure(int position) {
        super.onConfigure(position);
    }
}
