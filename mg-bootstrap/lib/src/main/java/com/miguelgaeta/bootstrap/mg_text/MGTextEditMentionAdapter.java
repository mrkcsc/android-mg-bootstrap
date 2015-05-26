package com.miguelgaeta.bootstrap.mg_text;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterSimple;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import java.util.Map;

import lombok.NonNull;
import lombok.Setter;

/**
 * Created by mrkcsc on 5/24/15.
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedDeclaration"})
public class MGTextEditMentionAdapter extends MGRecyclerAdapterSimple {

    @Setter
    private MGTextEdit.OnMentionsRecyclerItem onItem;

    @Setter
    private MGTextEdit editText;

    @Setter
    private Map<String, Object> tags;

    public MGTextEditMentionAdapter(@NonNull RecyclerView recycler) {
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

    /**
     * Look into the superset of all
     * available tags to fetch
     * associated data.
     */
    public Object getTagData(int position) {

        return tags.get(getTag(position));
    }

    public void mentionClicked(int position) {

        insertMention(editText, getTag(position));
    }

    private void insertMention(MGTextEdit editText, String tag) {

        // Fetch position of cursor.
        int position = editText.getCursorPosition();

        String lastToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (lastToken != null) {

            int positionStart = position - lastToken.length();

            // Insert tag, replacing any partial tag.
            editText.insert(tag + "  ", positionStart, position);

            // Selection hack to prevent next user inputted key
            // press from overwriting what just got input.
            editText.setSelection(positionStart + tag.length() + 1);
        }
    }
}
