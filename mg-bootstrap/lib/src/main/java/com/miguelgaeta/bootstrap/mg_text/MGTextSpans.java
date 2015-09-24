package com.miguelgaeta.bootstrap.mg_text;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 7/14/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGTextSpans {

    public enum Type {
        BOLD, BOLD_ITALIC, ITALIC, UNDERLINE, STRIKE_THROUGH
    }

    public static CharacterStyle get(Type type) {

        switch (type) {

            case BOLD:

                return new StyleSpan(Typeface.BOLD);

            case BOLD_ITALIC:

                return new StyleSpan(Typeface.BOLD_ITALIC);

            case ITALIC:

                return new StyleSpan(Typeface.ITALIC);

            case STRIKE_THROUGH:

                return new StrikethroughSpan();

            case UNDERLINE:

                return new UnderlineSpan();
        }

        throw new RuntimeException("Unrecognized type.");
    }

    public static CharacterStyle getClickable(Action1<View> onClick, @ColorInt int color, boolean underline) {

        return new ClickableSpan() {

            @Override
            public void onClick(View view) {

                if (onClick != null) {
                    onClick.call(view);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {

                drawState.setUnderlineText(underline);
                drawState.setColor(color);
            }
        };
    }

    public static CharacterStyle getBackgroundColor(@ColorInt int color) {

        return new BackgroundColorSpan(color);
    }
}
