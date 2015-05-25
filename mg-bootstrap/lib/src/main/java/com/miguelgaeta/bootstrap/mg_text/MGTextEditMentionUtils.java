package com.miguelgaeta.bootstrap.mg_text;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.widget.EditText;

import java.util.Collections;
import java.util.List;

import lombok.NonNull;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
class MGTextEditMentionUtils {

    private static final List<Character> identifiers = Collections.singletonList('@');

    public static int getPosition(EditText editText, @NonNull String string) {

        int position = string.length() - 1;

        if (editText != null && editText.getSelectionEnd() >= 0) {

            position = editText.getSelectionEnd();
        }

        return position;
    }

    /**
     * Fetch token using end of string or edit text
     * cursor position as the marker.
     */
    public static String getPartialMentionToken(EditText editText, @NonNull String string, @NonNull List<Character> identifiers) {

        int position = getPosition(editText, string);

        if (!string.isEmpty() && !Character.isWhitespace(string.charAt(position - 1))) {

            String[] tokens = string.split("\\s+");

            String lastToken = tokens[tokens.length - 1];

            for (Character identifier : identifiers) {

                if (lastToken.charAt(0) == identifier) {

                    return lastToken.substring(1, lastToken.length());
                }
            }
        }

        return null;
    }

    public static String getPartialMentionToken(EditText editText, @NonNull String string) {

        return getPartialMentionToken(editText, string, identifiers);
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
