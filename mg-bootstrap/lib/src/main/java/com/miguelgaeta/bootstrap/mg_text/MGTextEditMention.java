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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
class MGTextEditMention {

    @NonNull
    private MGTextEdit editText;

    private TextWatcher textWatcher;

    private MGTextEdit.OnMentionsMatchedListener onMentionsMatchedListener;

    private MGTextEditMentionAdapter adapter;
    private RecyclerView recyclerView;

    private MGTextEdit.OnMentionsStringify stringify;

    @NonNull
    private List<String> tags = new ArrayList<>();
    private List<String> tagsMatchedCache;
    private List         tagsData;

    public void setOnMentionsMatchedListener(MGTextEdit.OnMentionsMatchedListener onMentionsMatchedListener) {

        this.onMentionsMatchedListener = onMentionsMatchedListener;

        configure();
    }

    public void setMentionsData(List<String> tags, List tagsData, MGTextEdit.OnMentionsStringify stringify) {

        this.stringify = stringify;

        this.tags = tags;
        this.tagsData = tagsData;

        if (adapter != null) {
            adapter.setTagsData(tagsData);
        }

        configure();
    }

    /**
     * Get a list of entered mentions run
     * through the stringify function.
     */
    public List<String> getMentions() {

        List<String> mentions = new ArrayList<>();

        String text = editText.toString();

        for (int index = 0; index < tags.size(); index++) {

            String tag = "@" + tags.get(index);

            if (text.contains(tag) && stringify != null) {

                String tagStringified = stringify.stringify(index);

                if (!mentions.contains(tagStringified)) {
                     mentions.add(tagStringified);
                }
            }
        }

        return mentions;
    }

    /**
     * Configure the recycler view and standard
     * adapter to handle the mentions list.
     */
    public void setRecyclerView(RecyclerView recyclerView, MGTextEdit.OnMentionsRecyclerItem onItem) {

        adapter = MGRecyclerAdapter.configure(recyclerView, MGTextEditMentionAdapter.class);
        adapter.setOnItem(onItem);
        adapter.setEditText(editText);
        adapter.setTagsData(tagsData);

        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);

        configure();
    }

    private void configureTextWatcher() {

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
    public void processMentions(MGTextEdit editText, boolean force) {

        List<String> tagsMatched = new ArrayList<>();

        String partialMentionToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (partialMentionToken != null) {

            for (String tag : tags) {

                if (tag.contains(partialMentionToken)) {

                    tagsMatched.add(tag);
                }
            }
        }

        if (!tagsMatched.equals(tagsMatchedCache) || force) {

            if (onMentionsMatchedListener != null) {
                onMentionsMatchedListener.mentionsMatched(tagsMatched);
            }

            setAdapterData(adapter, tagsMatchedCache, tagsMatched);

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
     * Configure the mentions module by making sure the text
     * watched is initialized and do a pass on processing mentions.
     */
    private void configure() {

        configureTextWatcher();

        processMentions(editText, true);
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
}
