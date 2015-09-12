package com.miguelgaeta.bootstrap.mg_text;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

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
@AllArgsConstructor(staticName = "create")
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

        return buildSpannableString(sourceString, buildSpans());
    }

    private SpannableString buildSpannableString(@NonNull final String sourceString, @NonNull final List<Span> spans) {

        final SpannableString spannableString = new SpannableString(sourceString);

        for (Span span: spans) {

            for (CharacterStyle characterStyle : span.spanStyles) {

                spannableString.setSpan(characterStyle, span.getStart(), span.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    private List<Span> buildSpans() {

        // TODO

        return new ArrayList<>();
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

    @AllArgsConstructor(staticName = "create") @Getter
    public static class Match {

        private final int start;

        private final int end;

        private final String content;
    }

    @AllArgsConstructor(staticName = "create")
    private static class MatchStrategy {

        @NonNull
        private final String matchStart;

        @Nullable
        private final String matchEnd;

        @NonNull
        private final OnMatch onMatch;

        private final boolean matchEndRequired;
    }

    public interface OnMatch {

        Span call(Match match);
    }

    public static class Span {

        @Getter(value = AccessLevel.PRIVATE)
        private final String spanString;

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<CharacterStyle> spanStyles;

        @Getter @Setter(value = AccessLevel.PRIVATE)
        private int start;

        @Getter @Setter(value = AccessLevel.PRIVATE)
        private int end;

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
    }
}
