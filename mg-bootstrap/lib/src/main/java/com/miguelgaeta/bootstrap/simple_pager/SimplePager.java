package com.miguelgaeta.bootstrap.simple_pager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Miguel Gaeta on 10/21/15.
 */
@SuppressWarnings("unused")
public class SimplePager extends ViewPager {

    public SimplePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimplePager(Context context) {
        super(context);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);

        setOffscreenPageLimit(adapter.getCount());
    }

    @SuppressWarnings("unused")
    public void setTabLayout(@Nullable TabLayout tabLayout) {

        if (tabLayout != null) {
            tabLayout.setupWithViewPager(this);
        }
    }
}
