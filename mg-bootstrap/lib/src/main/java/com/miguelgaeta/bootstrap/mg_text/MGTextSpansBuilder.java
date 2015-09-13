package com.miguelgaeta.bootstrap.mg_text;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 9/11/15.
 */
@AllArgsConstructor(staticName = "create") @SuppressWarnings("UnusedDeclaration")
public class MGTextSpansBuilder {

    @NonNull
    private String sourceString;

    @NonNull @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<MatchStrategy> matchStrategies = new ArrayList<>();

    public void addMatchStrategy(@NonNull String matchStart, @NonNull String matchEnd, @NonNull OnMatch onMatch) {

        addMatchStrategy(matchStart, matchEnd, onMatch, true);
    }

    public void addMatchStrategy(@NonNull String matchStart, @NonNull String matchEnd, @NonNull OnMatch onMatch, boolean matchEndRequired) {

        matchStrategies.add(MatchStrategy.create(matchStart, matchEnd, onMatch, matchEndRequired));
    }

    /**
     * Looks for any instances of the start target and matches them. Since
     * no end target is specified, no delimiter is needed.
     */
    public void addMatchStrategy(@NonNull String matchStart, @NonNull OnMatch onMatch) {

        matchStrategies.add(MatchStrategy.create(matchStart, null, onMatch, false));
    }

    /**
     * Given replacement strategies, build an associated
     * spannable string.
     */
    public SpannableString build() {

        return buildSpannableString(buildSpans());
    }

    private SpannableString buildSpannableString(@NonNull final List<SpanMatch> spans) {

        final SpannableString spannableString = new SpannableString(sourceString);

        for (SpanMatch span: spans) {

            for (CharacterStyle characterStyle : span.getStyles()) {

                try {

                    spannableString.setSpan(characterStyle, span.getStart(), span.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                } catch (IndexOutOfBoundsException e) {

                    MGLog.i("Span cannot be applied, out of bounds.");
                }
            }
        }

        return spannableString;
    }

    private List<SpanMatch> buildSpans() {

        final List<SpanMatch> spans = new ArrayList<>();

        for (MatchStrategy matchStrategy : matchStrategies) {

            int startIndex = 0;

            do {

                startIndex = sourceString.indexOf(matchStrategy.getMatchStart(), startIndex);

                if (startIndex != -1) {

                    if (matchStrategy.getMatchEnd() == null) {

                        int endIndex = startIndex + matchStrategy.getMatchStart().length();

                        final Span span = matchStrategy.getOnMatch().call(Match.create(matchStrategy.getMatchStart()));

                        startIndex = computeStartIndexWithSpans(spans, startIndex, endIndex, span);

                    } else {

                        int endIndex = sourceString.indexOf(matchStrategy.getMatchEnd(), startIndex + matchStrategy.getMatchStart().length());

                        final boolean isEndOfStringMatch = endIndex == -1 && !matchStrategy.isMatchEndRequired();

                        if (isEndOfStringMatch) {

                            endIndex = sourceString.length();
                        }

                        if (endIndex != -1) {

                            final String match = sourceString.substring(startIndex + matchStrategy.getMatchStart().length(), endIndex);

                            final Span span = matchStrategy.getOnMatch().call(Match.create(match));

                            if (!isEndOfStringMatch) {

                                endIndex += matchStrategy.getMatchEnd().length();
                            }

                            startIndex = computeStartIndexWithSpans(spans, startIndex, endIndex, span);

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
    private int computeStartIndexWithSpans(final List<SpanMatch> spans, final int startIndex, final int endIndex, Span span) {

        // Replace match with user provided replacement.
        sourceString = new StringBuilder(sourceString).replace(startIndex, endIndex, span.getSpanString()).toString();

        // Update the new end index location.
        final int endIndexUpdated = startIndex + span.getSpanString().length();

        spans.add(SpanMatch.create(startIndex, endIndexUpdated, span.getSpanStyles()));

        final int offset = (endIndex - startIndex) - (endIndexUpdated - startIndex);

        if (offset != 0) {

            for (SpanMatch spanMatch : spans) {

                if (spanMatch.getStart() > endIndex) {

                    spanMatch.setStart(spanMatch.getStart() - offset);
                    spanMatch.setEnd(spanMatch.getEnd() - offset);
                }
            }
        }

        return endIndexUpdated;
    }

    /**
     * Build spannable string as an observable.
     */
    public Observable<SpannableString> buildObservable() {

        return Observable.create(subscriber -> {

            subscriber.onNext(build());
            subscriber.onCompleted();
        });
    }

    @AllArgsConstructor(staticName = "create", access = AccessLevel.PRIVATE) @Getter(value = AccessLevel.PRIVATE)
    private static class MatchStrategy {

        @NonNull
        private final String matchStart;

        @Nullable
        private final String matchEnd;

        @NonNull
        private final OnMatch onMatch;

        private final boolean matchEndRequired;
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
