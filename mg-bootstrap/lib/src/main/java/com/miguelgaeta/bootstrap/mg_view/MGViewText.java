package com.miguelgaeta.bootstrap.mg_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Miguel Gaeta on 4/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGViewText extends TextView {

    public MGViewText(Context context) {
        super(context);
    }

    public MGViewText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGViewText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Fix some probably undesired behavior in android where if
     * there is no text the view still has a height.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (getText() == null || getText().length() == 0) {

            heightMeasureSpec = View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.getMode(heightMeasureSpec));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
