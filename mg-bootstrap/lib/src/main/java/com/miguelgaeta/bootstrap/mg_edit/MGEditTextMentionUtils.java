package com.miguelgaeta.bootstrap.mg_edit;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

/**
 * Created by mrkcsc on 5/23/15.
 */
public class MGEditTextMentionUtils {

    /**
     * Apply bold span to a spannable string.
     */
    public static void applyBoldSpan(SpannableString spannableString, int startIndex, int endIndex) {

        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        spannableString.setSpan(bold, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
