package com.miguelgaeta.bootstrap.mg_text;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
public class MGTextEdit extends EditText {

    private final MGTextEditHasText hasText = new MGTextEditHasText(this);

    private final MGTextEditMention mention = new MGTextEditMention(this);

    public MGTextEdit(Context context) {
        super(context);
    }

    public MGTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGTextEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String toString() {

        return getText().toString().trim();
    }

    public void insert(CharSequence charSequence, int start, int end) {

        getText().replace(Math.min(start, end), Math.max(start, end), charSequence, 0, charSequence.length());
    }

    public void insert(CharSequence charSequence) {

        // See: http://bit.ly/1ArIVnX
        insert(charSequence, Math.max(getSelectionStart(), 0), Math.max(getSelectionEnd(), 0));
    }

    public void setOnHasTextListener(OnHasTextListener onHasTextListener) {

        hasText.setOnHasTextListener(onHasTextListener);
    }

    public void setOnMentionsMatchedListener(OnMentionsMatchedListener onMentionsMatchedListener) {

        mention.setOnMentionsMatchedListener(onMentionsMatchedListener);
    }

    public void setMentionsData(List<String> tags, List tagsData, OnMentionsStringify stringify) {

        mention.setMentionsData(tags, tagsData, stringify);
    }

    public void setMentionsRecycler(RecyclerView recycler, OnMentionsRecyclerItem onItem) {

        mention.setRecyclerView(recycler, onItem);
    }

    public interface OnMentionsMatchedListener {

        void mentionsMatched(List<String> tags);
    }

    public interface OnHasTextListener {

        void hasText(boolean hasText);
    }

    public interface OnMentionsRecyclerItem {

        MGTextEditMentionItem onItem(MGTextEditMentionAdapter adapter);
    }

    public interface OnMentionsStringify {

        String stringify(int position);
    }
}