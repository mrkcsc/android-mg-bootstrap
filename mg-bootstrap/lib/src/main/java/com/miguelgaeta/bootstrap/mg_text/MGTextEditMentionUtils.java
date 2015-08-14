package com.miguelgaeta.bootstrap.mg_text;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;

import java.util.Set;

import lombok.NonNull;

/**
 * Created by mrkcsc on 5/23/15.
 */
@SuppressWarnings("unused")
class MGTextEditMentionUtils {

    /**
     * Fetch token using end of string or edit text
     * cursor position as the marker.
     */
    public static String getPartialMentionToken(MGTextEdit editText, @NonNull Set<String> identifiers) {

        // Content string.
        String content = editText.toStringSafe();

        int position = editText.getCursorPosition();

        // If not at end of input and next character is not whitespace, no match.
        if (position != content.length() && content.charAt(position) != ' ') {

            return null;
        }

        for (String identifier : identifiers) {

            // Look for last identifier token before the cursor position.
            int lastIdentifier = content.substring(0, position).lastIndexOf(identifier);

            if (lastIdentifier != -1 && (lastIdentifier == 0 || content.charAt(lastIdentifier - 1) == ' ')) {

                // Return up to the current cursor position.
                return content.substring(lastIdentifier, position);
            }

        }

        return null;
    }

    /**
     * Fetch token using end of string or edit text
     * cursor position as the marker.
     */
    public static String getPartialMentionToken(MGTextEdit editText) {

        return getPartialMentionToken(editText, MGTextEditMention.getIdentifiers().keySet());
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
