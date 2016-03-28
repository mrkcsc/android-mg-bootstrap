package com.miguelgaeta.bootstrap.mg_text;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Created by Miguel Gaeta on 9/11/15.
 */
@AllArgsConstructor(staticName = "create") @SuppressWarnings("UnusedDeclaration")
public class MGTextSpansBuilder {

    @NonNull
    private String sourceString;

    private final List<MatchStrategy> matchStrategies = new ArrayList<>();

    public void addMatchStrategy(@NonNull OnMatch onMatch, @NonNull String start) {
        addMatchStrategy(onMatch, start, null);
    }

    public void addMatchStrategy(@NonNull OnMatch onMatch, @NonNull String start, @Nullable String end) {
        addMatchStrategy(onMatch, start, end, true);
    }

    public void addMatchStrategy(@NonNull OnMatch onMatch, @NonNull String start, @Nullable String end, boolean endRequired) {
        addMatchStrategy(onMatch, start, end, endRequired, false);
    }

    public void addMatchStrategy(@NonNull OnMatch onMatch, @NonNull String start, @Nullable String end, boolean endRequired, boolean endWithWhitespaceOrEOL) {
        matchStrategies.add(MatchStrategy.create(onMatch, start, end, endRequired, endWithWhitespaceOrEOL));
    }

    public SpannableString build() {

        return buildSpannableString(buildSpans());
    }

    private SpannableString buildSpannableString(@NonNull final List<SpanMatch> spans) {

        final SpannableString spannableString = new SpannableString(sourceString);

        for (SpanMatch span: spans) {

            try {

                for (CharacterStyle characterStyle : span.getStyles()) {

                    spannableString.setSpan(characterStyle, span.getStart(), span.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

            } catch (IndexOutOfBoundsException e) {

                Log.i("MGTextSpansBuilder", "Span cannot be applied, out of bounds: " + sourceString, e);
            }
        }

        return spannableString;
    }

    private List<SpanMatch> buildSpans() {

        final List<SpanMatch> spans = new ArrayList<>();

        for (MatchStrategy matchStrategy : matchStrategies) {

            int startIndex = 0;

            do {

                startIndex = sourceString.indexOf(matchStrategy.start, startIndex);

                if (startIndex != -1) {

                    final int startIndexOffset = matchStrategy.start.length();

                    if (matchStrategy.end == null) {

                        int endIndex = startIndex + startIndexOffset;

                        final Span span = matchStrategy.onMatch.call(Match.create(matchStrategy.start));

                        startIndex = computeStartIndexWithSpans(spans, startIndex, startIndexOffset, endIndex, span);

                    } else {

                        int endIndex = sourceString.indexOf(matchStrategy.end, startIndex + startIndexOffset);

                        final boolean isEOLMatch = endIndex == -1 && !matchStrategy.endRequired;

                        if (isEOLMatch) {

                            endIndex = sourceString.length();
                        }

                        if (matchStrategy.endWithWhitespaceOrEOL && endIndex != -1) {

                            if (endIndex != (sourceString.length() - 1) && !Character.isWhitespace(sourceString.charAt(endIndex + 1))) {

                                endIndex = -1;
                            }
                        }

                        if (endIndex != -1) {

                            final String match = sourceString.substring(startIndex + startIndexOffset, endIndex);

                            final Span span = matchStrategy.onMatch.call(Match.create(match));

                            if (!isEOLMatch) {

                                endIndex += matchStrategy.end.length();
                            }

                            startIndex = computeStartIndexWithSpans(spans, startIndex, startIndexOffset, endIndex, span);

                        } else {

                            startIndex = -1;
                        }
                    }
                }

            } while (startIndex != -1);
        }

        return spans;
    }

    /**
     * Given a user provided span and a current start and
     * end index.  Replace range with span result
     * and add to span match array.
     */
    private int computeStartIndexWithSpans(final List<SpanMatch> spans, final int startIndex, final int startIndexOffset, final int endIndex, Span span) {

        // Replace match with user provided replacement.
        sourceString = new StringBuilder(sourceString).replace(startIndex, endIndex, span.getSpanString()).toString();

        // Update the new end index location.
        final int endIndexUpdated = startIndex + span.getSpanString().length();

        final int offset = (endIndex - startIndex) - (endIndexUpdated - startIndex);

        if (offset != 0) {

            for (SpanMatch spanMatch : spans) {

                if (spanMatch.getStart() > startIndex) {
                    spanMatch.setStart(spanMatch.getStart() - startIndexOffset);

                    if (spanMatch.getStart() > endIndex) {
                        spanMatch.setStart(spanMatch.getStart() - (offset - startIndexOffset));
                    }
                }

                if (spanMatch.getEnd() > startIndex) {
                    spanMatch.setEnd(spanMatch.getEnd() - startIndexOffset);

                    if (spanMatch.getEnd() > endIndex) {
                        spanMatch.setEnd(spanMatch.getEnd() - (offset - startIndexOffset));
                    }
                }
            }
        }

        spans.add(SpanMatch.create(startIndex, endIndexUpdated, span.getSpanStyles()));

        return endIndexUpdated;
    }

    @AllArgsConstructor(staticName = "create", access = AccessLevel.PRIVATE)
    private static class MatchStrategy {

        @NonNull
        private final OnMatch onMatch;

        @NonNull
        private final String start;

        @Nullable
        private final String end;

        private final boolean endRequired;
        private final boolean endWithWhitespaceOrEOL;
    }

    @AllArgsConstructor(staticName = "create", access = AccessLevel.PRIVATE) @Getter(value = AccessLevel.PRIVATE)
    private static class SpanMatch {

        @Setter(value = AccessLevel.PRIVATE)
        private int start;

        @Setter(value = AccessLevel.PRIVATE)
        private int end;

        private List<CharacterStyle> styles;
    }

    public interface OnMatch {

        Span call(Match match);
    }

    /**
     * Represents a matched string used during the
     * span searching process of the original
     * string.
     */
    @AllArgsConstructor(staticName = "create") @Getter
    public static class Match {

        private final String match;
    }

    @Getter
    public static class Span {

        @SuppressWarnings("UnusedDeclaration")
        private final String spanString;

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection, UnusedDeclaration")
        private final List<CharacterStyle> spanStyles;

        private Span(String spanString, List<CharacterStyle> spanStyles) {

            this.spanString = spanString;
            this.spanStyles = spanStyles;
        }

        public static Span create(String spanString, CharacterStyle... spanStyles) {

            final List<CharacterStyle> styles = new ArrayList<>();

            Collections.addAll(styles, spanStyles);

            return Span.create(spanString, styles);
        }

        public static Span create(String spanString, List<CharacterStyle> spanStyles) {

            return new Span(spanString, spanStyles);
        }

        public static Span create(String spanString) {

            return new Span(spanString, new ArrayList<>());
        }
    }
}
