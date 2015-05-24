package com.miguelgaeta.bootstrap.mg_edit;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
public class MGEditTextMention {

    @NonNull
    private MGEditText editText;

    private TextWatcher textWatcher;

    private OnMentionsMatchedListener onMentionsMatchedListener;

    private Map<String, Object> mentionsMatches;

    private boolean processTextChanged = true;

    @NonNull
    private Map<String, Object> mentionsData = new HashMap<>();

    public void setOnMentionsMatchedListener(MGEditTextMention.OnMentionsMatchedListener onMentionsMatchedListener) {

        this.onMentionsMatchedListener = onMentionsMatchedListener;

        configureTextWatcher();

        processMentions(editText.getText(), true);
    }

    public void setMentionsData(Map<String, Object> mentionsData) {

        this.mentionsData = mentionsData;

        configureTextWatcher();

        processMentions(editText.getText(), true);
    }

    private void configureTextWatcher() {

        if (textWatcher == null) {
            textWatcher = new MGEditTextWatcher() {

                @Override
                public void afterTextChanged(Editable editable) {

                    if (processTextChanged) {

                        processMentions(editable, false);
                    }

                    processTextChanged = true;
                }
            };

            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Process out mentions given an arbitrary
     * editable object.
     */
    private void processMentions(Editable editable, boolean force) {

        Map<String, Object> mentionsMatches = new LinkedHashMap<>();

        String partialMentionToken = getPartialMentionToken(editable.toString());

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

        // Apply visual spans.
        applySpans(editable, mentionsData);
    }

    private void applySpans(Editable editable, Map<String, Object> mentionsData) {

        SpannableString spannableString = new SpannableString(editable.toString());

        processTextChanged = false;

        String[] tokens =  editable.toString().split(" ");

        int startIndex = 0;

        for (String token : tokens) {

            int endIndex = startIndex + token.length();

            if (mentionsData.containsKey(token.replace("@", ""))) {

                MGEditTextMentionUtils.applyBoldSpan(spannableString, startIndex, endIndex);
            }

            startIndex = endIndex + 1;
        }

        editText.setText(spannableString);
        editText.setSelection(spannableString.length());
    }

    /**
     * Given an editable, check to see if the
     * last token is a partial mention.
     */
    private String getPartialMentionToken(String editable) {

        if (editable.length() == 0 || Character.isWhitespace(editable.charAt(editable.length() - 1))) {

            return null;
        }

        String[] tokens = editable.split(" ");

        String lastToken = tokens[tokens.length - 1];

        return lastToken.charAt(0) == '@' ? lastToken.substring(1, lastToken.length()) : null;
    }

    public interface OnMentionsMatchedListener {

        void mentionsMatched(Map<String, Object> mentionsData);
    }
}
