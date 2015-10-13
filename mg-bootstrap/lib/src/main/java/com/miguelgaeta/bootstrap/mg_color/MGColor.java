package com.miguelgaeta.bootstrap.mg_color;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 10/12/15.
 */
public class MGColor {

    public static int fromResource(@NonNull Context context, @ColorRes int colorResourceId) {

        return ContextCompat.getColor(context, colorResourceId);
    }

    public static int fromTheme(@NonNull Context context, @AttrRes int attributeResourceId) {

        final TypedValue typedValue = new TypedValue();

        final Resources.Theme theme = context.getTheme();

        theme.resolveAttribute(attributeResourceId, typedValue, true);

        return typedValue.data;
    }
}
