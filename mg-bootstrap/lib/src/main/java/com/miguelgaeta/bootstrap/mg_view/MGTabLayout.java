package com.miguelgaeta.bootstrap.mg_view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import java.lang.reflect.Field;

/**
 * Created by Miguel Gaeta on 10/12/15.
 */
public class MGTabLayout extends TabLayout {

    public MGTabLayout(Context context) {
        super(context);
    }

    public MGTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        try {
            if (getTabCount() != 0) {

                Field field = TabLayout.class.getDeclaredField("mTabMinWidth");

                if (field != null) {
                    field.setAccessible(true);
                    field.set(this, (int) (getMeasuredWidth() / (float) getTabCount()));
                }
            }

        } catch (Exception e) {

            MGLog.i(e, "Unable to modify min tab width.");
        }
    }
}
