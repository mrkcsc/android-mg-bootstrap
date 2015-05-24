package com.miguelgaeta.bootstrap.mg_edit;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

/**
 * Created by mrkcsc on 5/23/15.
 */
class MGEditTextMentionUtils {

    /**
     * Given an editable, check to see if the
     * last token is a partial mention.
     */
    public static String getPartialMentionToken(String editable) {

        if (editable.length() == 0 || Character.isWhitespace(editable.charAt(editable.length() - 1))) {

            return null;
        }

        String[] tokens = editable.split(" ");

        String lastToken = tokens[tokens.length - 1];

        return lastToken.charAt(0) == '@' ? lastToken.substring(1, lastToken.length()) : null;
    }

    /**
     * Apply bold span to a spannable string.
     */
    public static void applyBoldSpan(SpannableString spannableString, int startIndex, int endIndex) {

        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        spannableString.setSpan(bold, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
