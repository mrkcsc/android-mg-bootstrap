package com.miguelgaeta.bootstrap.mg_text;

import android.support.annotation.LayoutRes;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

/**
 * Created by mrkcsc on 5/24/15.
 */
public class MGTextEditMentionItem extends MGRecyclerViewHolder<MGTextEditMentionAdapter> {

    public MGTextEditMentionItem(@LayoutRes int layout, MGTextEditMentionAdapter adapter) {
        super(layout, adapter);

        // Handle clicks.
        itemView.setOnClickListener(view -> getAdapter().mentionClicked(getAdapterPosition()));
    }

    @Override
    protected void onConfigure(int position) {
        super.onConfigure(position);
    }
}
