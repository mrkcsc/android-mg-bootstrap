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

    @Setter
    private MGEditText editText;

    public MGEditTextMentionAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    /**
     * When we need to create a view holder delegate
     * the initialization and configuration
     * to the callee.
     */
    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return onItem.onItem(this);
    }

    /**
     * Fetch data item at position.
     */
    public String getTag(int position) {

        return (String)getItem(position);
    }

    public void mentionClicked(int position) {

        insertMention(editText, getTag(position));
    }

    private void insertMention(MGEditText editText, String tag) {

        // Fetch position of cursor.
        int position = MGEditTextMentionUtils.getPosition(editText, editText.getText().toString());

        String lastToken = MGEditTextMentionUtils.getPartialMentionToken(editText, editText.getText().toString());

        if (lastToken != null) {

            // Insert tag, replacing any partial tag already in place.
            editText.insert(tag + " ", position - lastToken.length(), position);
        }
    }
}
