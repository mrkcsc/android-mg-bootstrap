package com.miguelgaeta.bootstrap.mg_text;

import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

/**
 * Created by Miguel Gaeta on 7/14/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGTextSpans {

    public static CharacterStyle getMonospace() {
        return new TypefaceSpan("monospace");
    }

    public static CharacterStyle getRelativeSize(float proportion) {
        return new RelativeSizeSpan(proportion);
    }

    public static CharacterStyle getClickable(@ColorInt int color, boolean underline, @Nullable View.OnClickListener onClick) {
        return new ClickableSpan() {

            @Override
            public void onClick(View view) {

                if (onClick != null) {
                    onClick.onClick(view);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint drawState) {

                drawState.setUnderlineText(underline);
                drawState.setColor(color);
            }
        };
    }

    public static CharacterStyle getForegroundColor(@ColorInt int color) {
        return new ForegroundColorSpan(color);
    }

    public static CharacterStyle getBold() {
        return new StyleSpan(Typeface.BOLD);
    }

    public static CharacterStyle getBoldItalic() {
        return new StyleSpan(Typeface.BOLD_ITALIC);
    }

    public static CharacterStyle getItalic() {
        return new StyleSpan(Typeface.ITALIC);
    }

    public static CharacterStyle getStrikethrough() {
        return new StrikethroughSpan();
    }

    public static CharacterStyle getUnderline() {
        return new UnderlineSpan();
    }

    public static CharacterStyle getBackgroundColor(@ColorInt int color) {
        return new BackgroundColorSpan(color);
    }
}
