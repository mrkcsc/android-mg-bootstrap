package com.miguelgaeta.bootstrap.mg_edit;

import android.text.Editable;
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
public class MGEditTextMentions {

    @NonNull
    private MGEditText editText;

    private TextWatcher textWatcher;

    private OnMentionsMatchedListener onMentionsMatchedListener;

    private Map<String, Object> mentionsMatches;

    @NonNull
    private Map<String, Object> mentionsData = new HashMap<>();

    public void setOnMentionsMatchedListener(MGEditTextMentions.OnMentionsMatchedListener onMentionsMatchedListener) {

        this.onMentionsMatchedListener = onMentionsMatchedListener;

        configureTextWatcher();

        processMentions(editText.getText());
    }

    public void setMentionsData(Map<String, Object> mentionsData) {

        this.mentionsData = mentionsData;

        configureTextWatcher();

        processMentions(editText.getText());
    }

    private void configureTextWatcher() {

        if (textWatcher == null) {
            textWatcher = new MGEditTextWatcher() {

                @Override
                public void afterTextChanged(Editable editable) {

                    processMentions(editable);
                }
            };

            editText.addTextChangedListener(textWatcher);
        }
    }

    /**
     * Process out mentions given an arbitrary
     * editable object.
     */
    private void processMentions(Editable editable) {

        Map<String, Object> mentionsMatches = new LinkedHashMap<>();

        String partialMentionToken = getPartialMentionToken(editable.toString());

        if (partialMentionToken != null) {

            for (String key : mentionsData.keySet()) {

                if (key.contains(partialMentionToken)) {

                    mentionsMatches.put(key, mentionsData.get(key));
                }
            }
        }

        if (onMentionsMatchedListener != null && !mentionsMatches.equals(this.mentionsMatches)) {
            onMentionsMatchedListener.mentionsMatched(mentionsMatches);

            this.mentionsMatches = mentionsMatches;
        }
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
