package com.miguelgaeta.bootstrap.mg_text;

import android.text.Editable;
import android.text.TextWatcher;

import com.miguelgaeta.bootstrap.views.LambdaTextWatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
class MGTextEditHasText {

    @NonNull
    private MGTextEdit editText;

    private MGTextEdit.OnHasTextListener onHasTextListener;

    private TextWatcher textWatcher;

    private boolean hasText;

    public void setOnHasTextListener(MGTextEdit.OnHasTextListener onHasTextListener) {

        this.onHasTextListener = onHasTextListener;

        configureOnHasTextListener();
    }

    /**
     * When a listener is provided, make sure to invalidate
     * any existing text watcher, then emit the initial
     * state followed by future changes.
     */
    private void configureOnHasTextListener() {

        if (textWatcher != null) {

            editText.removeTextChangedListener(textWatcher);
        }

        onHasText(editText.length(), true);

        textWatcher = new LambdaTextWatcher(new LambdaTextWatcher.OnAfterChanged() {
            @Override
            public void afterTextChanged(Editable editable) {
                onHasText(editable.length(), false);
            }
        });

        editText.addTextChangedListener(textWatcher);
    }

    /**
     * Given a length, infer if has text.  Only emit when
     * this changes from the previous value or
     * force flag is provided.
     */
    private void onHasText(int length, boolean force) {

        boolean hasText = length > 0;

        if (onHasTextListener != null && (this.hasText != hasText || force)) {
            onHasTextListener.hasText(length > 0);
        }

        this.hasText = hasText;
    }
}
