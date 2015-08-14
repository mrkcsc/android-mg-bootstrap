package com.miguelgaeta.bootstrap.mg_text;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextWatcher;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerDataPayload;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by mrkcsc on 5/23/15.
 */
public class MGTextEditMention<T> {

    @Setter @Getter
    private static Map<String, Integer> identifiers = getDefaultIdentifiers();

    private TextWatcher textWatcher;

    private MGTextEditMentionCallbacks<T> callbacks;
    private MGTextEdit editText;

    private MGTextEditMentionAdapter adapter;
    private RecyclerView recyclerView;

    private List<Map.Entry<String, T>> tagsMatchedCache;

    private Map<String, T> tags;

    public MGTextEditMention(@NonNull MGTextEdit editText, @NonNull RecyclerView recyclerView, @NonNull MGTextEditMentionCallbacks<T> callbacks) {

        editText.setMentionsModule(this);

        this.editText = editText;

        this.callbacks = callbacks;

        this.adapter = MGRecyclerAdapter.configure(new MGTextEditMentionAdapter(recyclerView));
        this.adapter.setCallbacks(callbacks);

        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);

        // Configure watcher.
        configureTextWatcher(editText);

        // Initial processing.
        processMentions(editText, true);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setMentionsData(Map<String, T> tags) {

        this.tags = tags;

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

            if (text.contains(tag.getKey())) {

                String toString = callbacks.tagDataToString(tag.getValue());

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

        List<Map.Entry<String, T>> tagsMatched = new ArrayList<>();

        String partialMentionToken = MGTextEditMentionUtils.getPartialMentionToken(editText);

        if (partialMentionToken != null) {
            partialMentionToken = partialMentionToken.toLowerCase();

            for (Map.Entry<String, T> entry : tags.entrySet()) {

                String tagLower = entry.getKey().toLowerCase();

                if (tagLower.contains(partialMentionToken) && !tagLower.equals(partialMentionToken)) {

                    tagsMatched.add(entry);
                }
            }
        }

        if (!tagsMatched.equals(tagsMatchedCache) || force) {

            callbacks.onTagsMatched(tagsMatched);

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
    private void setAdapterData(MGTextEditMentionAdapter adapter, List<Map.Entry<String, T>> dataOld, List<Map.Entry<String, T>> dataNew) {

        if (dataOld == null) {
            dataOld = new LinkedList<>();
        }

        MGRecyclerDataPayload payload = new MGRecyclerDataPayload();

        for (Map.Entry<String, T> entry : dataNew) {

            final String identifier = Character.toString(entry.getKey().charAt(0));

            final int viewType = identifiers.get(identifier);

            payload.add(viewType, entry.getKey(), entry);
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

            int startIndex = 0;

            while (indexOfTag(spannable, startIndex, entry.getKey()) != -1) {

                int tagStartIndex = indexOfTag(spannable, startIndex, entry.getKey());

                MGTextEditMentionUtils.applyBoldSpan(spannable, tagStartIndex, tagStartIndex + entry.getKey().length());

                // Update start index.
                startIndex = tagStartIndex + 1;
            }
        }
    }

    private int indexOfTag(Spannable spannable, int startIndex, String tag) {

        return spannable.toString().indexOf(tag, startIndex);
    }

    private static Map<String, Integer> getDefaultIdentifiers() {

        Map<String, Integer> identifiers = new HashMap<>();

        identifiers.put("@", 0);
        identifiers.put("+", 1);

        return identifiers;
    }
}
