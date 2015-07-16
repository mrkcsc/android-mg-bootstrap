package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.miguelgaeta.bootstrap.R;
import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;
import com.miguelgaeta.bootstrap.mg_strings.MGStrings;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLifecycleContentView {

    public static final String INTENT_CONTENT_VIEW = "INTENT_CONTENT_VIEW";

    /**
     * Sets the content view of an activity automatically if it is
     * found. Otherwise, does nothing.
     */
    static void setContentView(@NonNull Activity activity) {

        Integer contentView;

        if (activity.getIntent().hasExtra(INTENT_CONTENT_VIEW)) {

            contentView = activity.getIntent().getIntExtra(INTENT_CONTENT_VIEW, -1);

        } else {

            contentView = MGLifecycleContentView.getContentView(activity.getClass());
        }

        if (contentView != null) {

            activity.setContentView(contentView);
        }
    }

    /**
     * Attempts to fetch the content view of the fragment,
     * returns the unmodified input view if not found.
     */
    static View getContentView(@NonNull Fragment fragment, ViewGroup container, View view) {

        Integer contentView = MGLifecycleContentView.getContentView(fragment.getClass());

        if (contentView != null) {

            view = fragment.getActivity().getLayoutInflater().inflate(contentView, container, false);
        }

        return view;
    }

    /**
     * Fetch the content view of an activity using a
     * convention based naming approach - the activity name
     * lower cased and underscored.
     */
    private static Integer getContentView(@NonNull Class classObject) {

        String className = classObject.getSimpleName();
        String classNameFormatted = MGStrings.camelCaseToLowerCaseUnderscores(className);

        return MGReflection.getResourceId(classNameFormatted, R.layout.class);
    }
}
