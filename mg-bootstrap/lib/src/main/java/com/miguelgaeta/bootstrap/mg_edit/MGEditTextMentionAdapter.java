package com.miguelgaeta.bootstrap.mg_edit;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterSimple;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import lombok.NonNull;
import lombok.Setter;

/**
 * Created by mrkcsc on 5/24/15.
 */
public class MGEditTextMentionAdapter extends MGRecyclerAdapterSimple {

    @Setter
    private MGEditText.OnMentionsRecyclerItem onItem;

    public MGEditTextMentionAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    /**
     * When we need to create a view holder delgate
     * the initialization and configuration
     * to the callee.
     */
    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return onItem.onItem(this);
    }
}
