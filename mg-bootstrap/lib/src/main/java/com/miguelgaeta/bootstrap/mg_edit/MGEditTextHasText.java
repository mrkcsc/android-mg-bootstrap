package com.miguelgaeta.bootstrap.mg_edit;

import android.text.Editable;
import android.text.TextWatcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Created by mrkcsc on 5/23/15.
 */
@RequiredArgsConstructor
public class MGEditTextHasText {

    @NonNull
    private MGEditText editText;

    private OnHasTextListener onHasTextListener;

    private TextWatcher onHasTextWatcher;

    private boolean hasText;

    public void setOnHasTextListener(OnHasTextListener onHasTextListener) {

        this.onHasTextListener = onHasTextListener;

        configureOnHasTextListener();
    }

    /**
     * When a listener is provided, make sure to invalidate
     * any existing text watcher, then emit the initial
     * state followed by future changes.
     */
    private void configureOnHasTextListener() {

        if (onHasTextWatcher != null) {

            editText.removeTextChangedListener(onHasTextWatcher);
        }

        onHasText(editText.length(), true);

        onHasTextWatcher = new MGEditTextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {

                onHasText(editable.length(), false);
            }
        };

        editText.addTextChangedListener(onHasTextWatcher);
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

    /**
     * Used to emit has text events.
     */
    public interface OnHasTextListener {

        void hasText(boolean hasText);
    }
}
