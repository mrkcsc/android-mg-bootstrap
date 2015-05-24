package com.miguelgaeta.bootstrap.mg_edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
public class MGEditText extends EditText {

    private final MGEditTextHasText hasText = new MGEditTextHasText(this);

    private final MGEditTextMentions mentions = new MGEditTextMentions(this);

    public MGEditText(Context context) {
        super(context);
    }

    public MGEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnHasTextListener(MGEditTextHasText.OnHasTextListener onHasTextListener) {

        hasText.setOnHasTextListener(onHasTextListener);
    }
}
