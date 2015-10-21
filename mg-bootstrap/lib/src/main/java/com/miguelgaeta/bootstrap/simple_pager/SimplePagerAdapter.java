package com.miguelgaeta.bootstrap.simple_pager;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import rx.functions.Func0;

/**
 * Created by Miguel Gaeta on 10/21/15.
 */
@SuppressWarnings("unused")
public abstract class SimplePagerAdapter extends FragmentPagerAdapter {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final List<Item> items = onCreateItems();

    protected final Context context;

    @SuppressWarnings("unused")
    public SimplePagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (getItems().size() > position) {

            return getItems().get(position).get.call();
        }

        throw new RuntimeException("Unknown settings pager item at position: " + position + ".");
    }

    @Override
    public int getCount() {

        return getItems().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (getItems().size() > position) {

            return getItems().get(position).title;
        }

        return super.getPageTitle(position);
    }

    protected abstract @NonNull List<Item> onCreateItems();

    @SuppressWarnings("unused")
    protected String getString(@StringRes int id) {

        return context.getResources().getString(id);
    }

    @RequiredArgsConstructor(staticName = "create")
    public static class Item {

        private final String title;

        private final Func0<Fragment> get;
    }
}
