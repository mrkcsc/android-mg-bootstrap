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

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by mrkcsc on 5/23/15.
 */
public class MGTextEditMention<T> {

    private TextWatcher textWatcher;

    private OnMentionsMatchedListener onMentionsMatchedListener;

    private MGTextEditMentionAdapter adapter;
    private RecyclerView recyclerView;

    private OnMentionsStringify stringify;

    @NonNull @Getter
    private List<String> tags = new ArrayList<>();
    private List<String> tagsMatchedCache;

    private Map<String, T> rawTags;

    public MGTextEditMention(@NonNull MGTextEdit editText, RecyclerView recyclerView, MGTextEditMentionItem.OnItem onItem, OnMentionsMatchedListener onMatched) {

        editText.setMentionsModule(this);

        this.adapter = MGRecyclerAdapter.configure(new MGTextEditMentionAdapter(recyclerView));
        this.adapter.setOnItem(onItem);
        this.adapter.setEditText(editText);
        this.adapter.setTags(rawTags);

        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);

        this.onMentionsMatchedListener = onMatched;

        // Configure watcher.
        configureTextWatcher(editText);

        // Initial processing.
        processMentions(editText, true);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setMentionsData(@NonNull MGTextEdit editText, Map<String, T> tags, OnMentionsStringify stringify) {

        this.stringify = stringify;

        this.tags = new ArrayList<>(tags.keySet());

        this.rawTags = tags;

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

        for (String tag : tags) {

            if (text.contains("@" + tag) && stringify != null) {

                String tagStringified = stringify.stringify(tag);

                if (!mentions.contains(tagStringified)) {
                     mentions.add(tagStringified);
                }
            }
        }

        return mentions;
    }

    private void configureTextWatcher(@NonNull MGTextEdit editText) {

        if (textWatcher == null) {
            textWatcher = new MGTextEditWatcher() {

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    applySpan((Spannable) charSequence, tags);
                }
            };

            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Process out mentions given an arbitrary
     * editable object.
     */
    public void processMentions(@NonNull MGTextEdit editText, boolean force) {

        List<String> tagsMatched = new ArrayList<>();

        String partialMentionToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (partialMentionToken != null) {
            partialMentionToken = partialMentionToken.toLowerCase();

            for (String tag : tags) {

                String tagLower = tag.toLowerCase();

                if (tagLower.contains(partialMentionToken) && !tagLower.equals(partialMentionToken)) {

                    tagsMatched.add(tag);
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
    private void applySpan(Spannable spannable, List<String> tags) {

        // Remove existing spans.
        MGTextEditMentionUtils.removeSpans(spannable);

        for (String tag : tags) {

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

    public interface OnMentionsStringify {

        String stringify(String tag);
    }
}
