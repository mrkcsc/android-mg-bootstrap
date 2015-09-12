package com.miguelgaeta.bootstrap.mg_text;

import android.text.SpannableString;
import android.text.style.CharacterStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 9/11/15.
 */
public class MGTextSpansBuilder {

    private String sourceString;

    public void addReplacement(String targetStart, String targetEnd, Replacement replacement, boolean targetEndRequired) {

    }

    public void addReplacement(String targetStart, String targetEnd, Replacement replacement) {

        addReplacement(targetStart, targetEnd, replacement, true);
    }

    public void addReplacement(String target, Replacement replacement) {

    }

    /**
     * Given replacement strategies, build an associated
     * spannable string.
     */
    public SpannableString build() {

        return null;
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
    public static class Span {

        private final String spanString;

        private final List<CharacterStyle> spanStyles;

        /**
         * In addition to the standard constructor,
         * allow instantiation via a collated
         * array of character styles.
         */
        public static Span create(String spanString, CharacterStyle... spanStyles) {

            final List<CharacterStyle> styles = new ArrayList<>();

            Collections.addAll(styles, spanStyles);

            return Span.create(spanString, styles);
        }
    }

    public interface Replacement {

        Span call(Match match);
    }
}
