package com.miguelgaeta.bootstrap.mg_text;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;

import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 7/14/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGTextSpans {

    public static StyleSpan getBoldSpan() {

        return new StyleSpan(Typeface.BOLD);
    }

    public static StyleSpan getBoldItalicSpan() {

        return new StyleSpan(Typeface.BOLD_ITALIC);
    }

    public static StyleSpan getItalicSpan() {

        return new StyleSpan(Typeface.ITALIC);
    }

    public static StrikethroughSpan getStrikeThroughSpan() {

        return new StrikethroughSpan();
    }

    public static BackgroundColorSpan getBackgroundColorSpan(@NonNull Context context, @ColorRes int color) {

        return new BackgroundColorSpan(context.getResources().getColor(color));
    }

    public static ClickableSpan getClickableSpan(@NonNull Context context, Action1<View> onClick, @ColorRes int color, boolean underline) {

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
                drawState.setColor(context.getResources().getColor(color));
            }
        };
    }
}
