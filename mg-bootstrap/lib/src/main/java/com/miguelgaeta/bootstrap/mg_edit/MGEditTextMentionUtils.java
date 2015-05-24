package com.miguelgaeta.bootstrap.mg_edit;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
class MGEditTextMentionUtils {

    /**
     * Given an editable, check to see if the
     * last token is a partial mention.
     */
    public static String getPartialMentionToken(String string, int position) {

        MGLog.e("Position: " + position);

        if (string.length() > 0 && !Character.isWhitespace(string.charAt(string.length() - 1))) {

            String[] tokens = string.split(" ");

            String lastToken = tokens[tokens.length - 1];

            if (lastToken.charAt(0) == '@') {

                return lastToken.substring(1, lastToken.length());
            }
        }

        return null;
    }

    /**
     * Apply bold span to a spannable string.
     */
    public static void applyBoldSpan(Spannable spannable, int startIndex, int endIndex) {

        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        spannable.setSpan(bold, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * Remove all spans.
     */
    public static void removeSpans(Spannable spannable) {

        Object spansToRemove[] = spannable.getSpans(0, spannable.length(), Object.class);

        for (Object span : spansToRemove) {

            if (span instanceof CharacterStyle) {

                spannable.removeSpan(span);
            }
        }
    }
}
