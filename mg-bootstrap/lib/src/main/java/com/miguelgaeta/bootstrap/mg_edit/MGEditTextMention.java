package com.miguelgaeta.bootstrap.mg_edit;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

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

    private RecyclerView recyclerView;

    @NonNull
    private Map<String, Object> mentionsData = new HashMap<>();

    public void setOnMentionsMatchedListener(MGEditText.OnMentionsMatchedListener onMentionsMatchedListener) {

        this.onMentionsMatchedListener = onMentionsMatchedListener;

        configureTextWatcher();

        processMentions(editText, editText.getText().toString(), true);
    }

    public void setMentionsData(Map<String, Object> mentionsData) {

        this.mentionsData = mentionsData;

        configureTextWatcher();

        processMentions(editText, editText.getText().toString(), true);
    }

    public void setRecyclerView(RecyclerView recyclerView) {

        this.recyclerView = recyclerView;

        MGLog.e("Recycler: " + recyclerView);
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

            this.mentionsMatches = mentionsMatches;
        }
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
