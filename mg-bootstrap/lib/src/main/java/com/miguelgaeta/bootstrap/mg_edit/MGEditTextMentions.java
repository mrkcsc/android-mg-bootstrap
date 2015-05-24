package com.miguelgaeta.bootstrap.mg_edit;

import java.util.HashMap;
import java.util.Map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
public class MGEditTextMentions {

    @NonNull
    private MGEditText editText;

    @Setter
    private OnMentionsMatchedListener onMentionsMatchedListener;

    @Setter @NonNull
    private Map<String, Object> mentionsData = new HashMap<>();

    public interface OnMentionsMatchedListener {

        void mentionsMatched(Map<String, Object> mentionsData);
    }
}
