package com.miguelgaeta.bootstrap.mg_text;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterSimple;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import java.util.Map;

import lombok.Setter;

/**
 * Created by mrkcsc on 5/24/15.
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration"})
public class MGTextEditMentionAdapter extends MGRecyclerAdapterSimple {

    @Setter
    private MGTextEditMentionCallbacks callbacks;

    public MGTextEditMentionAdapter(RecyclerView recycler) {
        super(recycler);
    }

    /**
     * When we need to create a view holder delegate
     * the initialization and configuration
     * to the callee.
     */
    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return callbacks.onBindViewHolder(this, viewType);
    }

    /**
     * Fetch data item at position.
     */
    @SuppressWarnings("unchecked")
    public Map.Entry<String, ?> getTag(int position) {

        return (Map.Entry)getItem(position);
    }

    /**
     * Look into the superset of all
     * available tags to fetch
     * associated data.
     */
    public Object getTagData(int position) {

        return getTag(position).getValue();
    }

    public void mentionClicked(int position) {

        callbacks.onTagClicked(getTag(position).getKey());
    }
}
