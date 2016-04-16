package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    public static void setContentView(@NonNull Activity activity) {

        Integer contentView;

        if (activity.getIntent().hasExtra(INTENT_CONTENT_VIEW)) {

            contentView = activity.getIntent().getIntExtra(INTENT_CONTENT_VIEW, -1);

        } else {

            contentView = MGLifecycleContentView.getContentView(activity, activity.getClass());
        }

        if (contentView != null) {

            activity.setContentView(contentView);
        }
    }

    /**
     * Attempts to fetch the content view of the fragment,
     * returns the unmodified input view if not found.
     */
    public static View getContentView(@NonNull Fragment fragment, @NonNull LayoutInflater inflater, ViewGroup container) {

        View view = null;

        Integer contentView = MGLifecycleContentView.getContentView(fragment.getContext(), fragment.getClass());

        if (contentView != null) {

            view = inflater.inflate(contentView, container, false);
        }

        if (view == null) {

            throw new RuntimeException("Unable to inflate fragment content view: " + fragment.getClass().getName() + ", with resource id: " + contentView);
        }

        return view;
    }

    /**
     * Fetch the content view of an activity using a
     * convention based naming approach - the activity name
     * lower cased and underscored.
     */
    public static Integer getContentView(@NonNull Context context, @NonNull Class classObject) {

        final String className = classObject.getSimpleName();
        final String classNameFormatted = MGStrings.camelCaseToLowerCaseUnderscores(className);

        return MGReflection.getResourceId(context, classNameFormatted, R.layout.class);
    }
}
