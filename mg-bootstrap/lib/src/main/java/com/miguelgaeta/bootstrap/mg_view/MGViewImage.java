package com.miguelgaeta.bootstrap.mg_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Miguel Gaeta on 4/2/15.
 *
 * This image view subclass properly respects the max height
 * attribute (as it should - thanks android).
 */
public class MGViewImage extends ImageView {

    private int maxHeight;

    public MGViewImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            final int[] ids = {android.R.attr.maxHeight};
            final TypedArray array = context.obtainStyledAttributes(attrs, ids);

            if (array != null) {
                maxHeight = array.getDimensionPixelSize(0, 0);
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (maxHeight > 0 && maxHeight < MeasureSpec.getSize(heightMeasureSpec)) {

            heightMeasureSpec = MeasureSpec
                .makeMeasureSpec(maxHeight, MeasureSpec.getMode(heightMeasureSpec));
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
