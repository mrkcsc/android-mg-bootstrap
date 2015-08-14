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

    public interface OnBindViewHolder {

        /**
         * Required function invoked to obtained a valid
         * view holder for the associated adapter and
         * view type.
         */
        MGTextEditMentionItem onBindViewHolder(MGTextEditMentionAdapter adapter, int viewType);
    }

    public interface TagClicked {

        /**
         * Invoke when an associated item from the
         * adapter is clicked by the user.
         */
        void onTagClicked(String tag);
    }

    @Setter
    private MGTextEditMention mention;

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

        if (mention.getOnBindViewHolder() == null) {

            throw new RuntimeException("A valid view holder must be bound.");
        }

        return mention.getOnBindViewHolder().onBindViewHolder(this, viewType);
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

        if (mention.getOnTagClicked() != null) {
            mention.getOnTagClicked().onTagClicked(getTag(position).getKey());
        }
    }
}
