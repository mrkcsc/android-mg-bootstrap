package com.miguelgaeta.bootstrap.views;

import android.text.Editable;
import android.text.TextWatcher;

import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
public class LambdaTextWatcher implements TextWatcher {

    private final OnAfterChanged onAfterChanged;
    private final OnBeforeChanged onBeforeChanged;
    private final OnChanged onChanged;

    public LambdaTextWatcher() {
        this(null, null, null);
    }

    public LambdaTextWatcher(OnAfterChanged onAfterChanged) {
        this(onAfterChanged, null, null);
    }

    public LambdaTextWatcher(OnBeforeChanged onBeforeChanged) {
        this(null, onBeforeChanged, null);
    }

    public LambdaTextWatcher(OnChanged onChanged) {
        this(null, null, onChanged);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (onBeforeChanged != null) {
            onBeforeChanged.beforeTextChanged(charSequence, i, i1, i2);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if (onChanged != null) {
            onChanged.onTextChanged(charSequence, i, i1, i2);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (onAfterChanged != null) {
            onAfterChanged.afterTextChanged(editable);
        }
    }

    public interface OnAfterChanged {

        void afterTextChanged(Editable editable);
    }

    public interface OnBeforeChanged {

        void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);
    }

    public interface OnChanged {

        void onTextChanged(CharSequence charSequence, int i, int i1, int i2);
    }
}
