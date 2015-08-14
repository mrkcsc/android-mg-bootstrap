package com.miguelgaeta.bootstrap.mg_text;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextWatcher;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerDataPayload;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.NonNull;

/**
 * Created by mrkcsc on 5/23/15.
 */
public class MGTextEditMention<T> {

    private TextWatcher textWatcher;

    private OnMentionsMatchedListener onMentionsMatchedListener;

    private MGTextEditMentionAdapter adapter;
    private RecyclerView recyclerView;

    private OnMentionsStringify<T> stringify;

    private List<String> tagsMatchedCache;

    private Map<String, T> tags;

    public MGTextEditMention(@NonNull MGTextEdit editText, RecyclerView recyclerView, MGTextEditMentionItem.OnItem onItem, OnMentionsMatchedListener onMatched, OnMentionsStringify<T> stringify) {

        editText.setMentionsModule(this);

        this.adapter = MGRecyclerAdapter.configure(new MGTextEditMentionAdapter(recyclerView));
        this.adapter.setOnItem(onItem);
        this.adapter.setTags(tags);

        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);

        this.stringify = stringify;

        this.onMentionsMatchedListener = onMatched;

        // Configure watcher.
        configureTextWatcher(editText);

        // Initial processing.
        processMentions(editText, true);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setMentionsData(@NonNull MGTextEdit editText, Map<String, T> tags) {

        this.tags = tags;

        if (adapter != null) {
            adapter.setTags(tags);
        }

        // Re-process mentions.
        processMentions(editText, true);
    }

    /**
     * Get a list of entered mentions run
     * through the stringify function.
     */
    @SuppressWarnings("UnusedDeclaration")
    public List<String> getMentions(@NonNull String text) {

        List<String> mentions = new ArrayList<>();

        for (Map.Entry<String, T> tag : tags.entrySet()) {

            if (text.contains("@" + tag) && stringify != null) {

                String toString = stringify.toString(tag.getValue());

                if (!mentions.contains(toString)) {
                     mentions.add(toString);
                }
            }
        }

        return mentions;
    }

    /**
     * Insert a mention tag into the edit text,
     * replacing any current partial
     * mention entered.
     */
    @SuppressWarnings("UnusedDeclaration")
    public void insertMention(MGTextEdit editText, @NonNull String mention) {

        // Fetch position of cursor.
        int position = editText.getCursorPosition();

        String lastToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (lastToken != null) {

            int positionStart = position - lastToken.length();

            // Insert tag, replacing any partial tag.
            editText.insert(mention + "  ", positionStart, position);

            // Selection hack to prevent next user inputted key
            // press from overwriting what just got input.
            editText.setSelection(positionStart + mention.length() + 1);
        }
    }

    private void configureTextWatcher(@NonNull MGTextEdit editText) {

        if (textWatcher == null) {
            textWatcher = new MGTextEditWatcher() {

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    applySpan((Spannable) charSequence);
                }
            };

            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Process out mentions given an arbitrary
     * editable object.
     */
    void processMentions(@NonNull MGTextEdit editText, boolean force) {

        List<String> tagsMatched = new ArrayList<>();

        String partialMentionToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (partialMentionToken != null) {
            partialMentionToken = partialMentionToken.toLowerCase();

            for (Map.Entry<String, T> entry : tags.entrySet()) {

                String tagLower = entry.getKey().toLowerCase();

                if (tagLower.contains(partialMentionToken) && !tagLower.equals(partialMentionToken)) {

                    tagsMatched.add(entry.getKey());
                }
            }
        }

        if (!tagsMatched.equals(tagsMatchedCache) || force) {

            if (onMentionsMatchedListener != null) {
                onMentionsMatchedListener.mentionsMatched(tagsMatched);
            }

            if (adapter != null) {

                setAdapterData(adapter, tagsMatchedCache, tagsMatched);
            }

            // Update cached value.
            tagsMatchedCache = tagsMatched;
        }
    }

    /**
     * Set the adapter data and also make
     * sure the recycler view height
     * is correct.
     */
    private void setAdapterData(MGTextEditMentionAdapter adapter, List<String> dataOld, List<String> dataNew) {

        if (dataOld == null) {
            dataOld = new LinkedList<>();
        }

        MGRecyclerDataPayload payload = new MGRecyclerDataPayload();

        for (String tag : dataNew) {

            payload.add(0, tag, tag);
        }

        int heightOld = MGReflection.dipToPixels(Math.min(36 * dataOld.size(), 144));

        int heightNew = MGReflection.dipToPixels(Math.min(36 * dataNew.size(), 144));

        // Run animation.
        MGTextEditMentionAnimations.create(recyclerView, heightOld, heightNew);

        // Update data source.
        adapter.setData(payload.getList());
    }

    /**
     * Apply spans to spannable string.
     */
    private void applySpan(Spannable spannable) {

        // Remove existing spans.
        MGTextEditMentionUtils.removeSpans(spannable);

        for (Map.Entry<String, T> entry : tags.entrySet()) {

            String tag = entry.getKey();

            tag = "@" + tag;

            int startIndex = 0;

            while (spannable.toString().indexOf(tag, startIndex) != -1) {

                int tagStartIndex = spannable.toString().indexOf(tag, startIndex);

                MGTextEditMentionUtils.applyBoldSpan(spannable, tagStartIndex, tagStartIndex + tag.length());

                // Update start index.
                startIndex = tagStartIndex + 1;
            }
        }
    }

    public interface OnMentionsMatchedListener {

        void mentionsMatched(List<String> tags);
    }

    public interface OnMentionsStringify<T> {

        String toString(T data);
    }
}
