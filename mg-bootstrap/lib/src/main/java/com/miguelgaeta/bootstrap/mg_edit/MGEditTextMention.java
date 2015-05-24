package com.miguelgaeta.bootstrap.mg_edit;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerDataPayload;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
class MGEditTextMention {

    @NonNull
    private MGEditText editText;

    private TextWatcher textWatcher;

    private MGEditText.OnMentionsMatchedListener onMentionsMatchedListener;

    private Map<String, Object> mentionsMatches;

    private MGEditTextMentionAdapter adapter;
    private RecyclerView recyclerView;

    @NonNull
    private Map<String, Object> mentionsData = new HashMap<>();

    public void setOnMentionsMatchedListener(MGEditText.OnMentionsMatchedListener onMentionsMatchedListener) {

        this.onMentionsMatchedListener = onMentionsMatchedListener;

        configure();
    }

    public void setMentionsData(Map<String, Object> mentionsData) {

        this.mentionsData = mentionsData;

        configure();
    }

    /**
     * Configure the recycler view and standard
     * adapter to handle the mentions list.
     */
    public void setRecyclerView(RecyclerView recyclerView, @LayoutRes int layoutId) {

        adapter = MGRecyclerAdapter.configure(recyclerView, MGEditTextMentionAdapter.class);
        adapter.setLayoutId(layoutId);

        this.recyclerView = recyclerView;
        this.recyclerView.setItemAnimator(null);

        configure();
    }

    private void configureTextWatcher() {

        if (textWatcher == null) {
            textWatcher = new MGEditTextWatcher() {

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    applySpan((Spannable) charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    processMentions(editText, editable.toString(), false);
                }
            };

            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Process out mentions given an arbitrary
     * editable object.
     */
    private void processMentions(EditText editText, String string, boolean force) {

        Map<String, Object> mentionsMatches = new LinkedHashMap<>();

        String partialMentionToken = MGEditTextMentionUtils.getPartialMentionToken(editText, string, Collections.singletonList('@'));

        if (partialMentionToken != null) {

            for (String key : mentionsData.keySet()) {

                if (key.contains(partialMentionToken)) {

                    mentionsMatches.put(key, mentionsData.get(key));
                }
            }
        }

        if (!mentionsMatches.equals(this.mentionsMatches) || force) {

            if (onMentionsMatchedListener != null) {
                onMentionsMatchedListener.mentionsMatched(mentionsMatches);
            }

            setAdapterData(adapter, mentionsMatches);

            this.mentionsMatches = mentionsMatches;
        }
    }

    private void setAdapterData(MGEditTextMentionAdapter adapter, Map<String, Object> mentionsMatches) {

        MGRecyclerDataPayload payload = new MGRecyclerDataPayload();

        for (String key : mentionsMatches.keySet()) {

            payload.add(0, key, key);
        }

        int height = 36 * mentionsMatches.size();

        // Set the height.
        recyclerView.getLayoutParams().height = MGReflection.dipToPixels(Math.min(height, 144));

        // Set the visibility.
        recyclerView.setVisibility(mentionsMatches.size() > 0 ? View.VISIBLE : View.GONE);

        adapter.setData(payload.getList());
    }

    /**
     * Configure the mentions module by making sure the text
     * watched is initialized and do a pass on processing mentions.
     */
    private void configure() {

        configureTextWatcher();

        processMentions(editText, editText.getText().toString(), true);
    }

    private void applySpan(Spannable spannable) {

        // Remove existing spans.
        MGEditTextMentionUtils.removeSpans(spannable);

        String[] tokens =  spannable.toString().split(" ");

        int startIndex = 0;

        for (String token : tokens) {

            int endIndex = startIndex + token.length();

            token = token.replace("@", "");

            if (!token.isEmpty() && mentionsData.containsKey(token)) {

                MGEditTextMentionUtils.applyBoldSpan(spannable, startIndex, endIndex);
            }

            startIndex = endIndex + 1;
        }
    }
}
