package com.miguelgaeta.bootstrap.mg_edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Map;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
public class MGEditText extends EditText {

    private final MGEditTextHasText hasText = new MGEditTextHasText(this);

    private final MGEditTextMention mention = new MGEditTextMention(this);

    public MGEditText(Context context) {
        super(context);
    }

    public MGEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void insert(CharSequence charSequence, int start, int end) {

        getText().replace(Math.min(start, end), Math.max(start, end), charSequence, 0, charSequence.length());
    }

    public void insert(CharSequence charSequence) {

        // See: http://bit.ly/1ArIVnX
        insert(charSequence, Math.max(getSelectionStart(), 0), Math.max(getSelectionEnd(), 0));
    }

    public void setOnHasTextListener(MGEditTextHasText.OnHasTextListener onHasTextListener) {

        hasText.setOnHasTextListener(onHasTextListener);
    }

    public void setOnMentionsMatchedListener(MGEditTextMention.OnMentionsMatchedListener onMentionsMatchedListener) {

        mention.setOnMentionsMatchedListener(onMentionsMatchedListener);
    }

    public void setMentionsData(Map<String, Object> mentionsData) {

        mention.setMentionsData(mentionsData);
    }
}
