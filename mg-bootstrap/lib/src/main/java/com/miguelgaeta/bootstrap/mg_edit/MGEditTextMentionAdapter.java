package com.miguelgaeta.bootstrap.mg_edit;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterSimple;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import lombok.NonNull;
import lombok.Setter;

/**
 * Created by mrkcsc on 5/24/15.
 */
class MGEditTextMentionAdapter extends MGRecyclerAdapterSimple {

    @Setter
    private @LayoutRes int layoutId;

    public MGEditTextMentionAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MGEditTextMentionItem(layoutId, this);
    }
}
