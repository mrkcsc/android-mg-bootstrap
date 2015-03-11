package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import lombok.Getter;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 12/2/14.
 */
@SuppressWarnings("unused")
public class MGLifecycleFragment extends Fragment {

    @Getter
    // Configuration object for the fragment.
    private MGLifecycleFragmentConfig config = new MGLifecycleFragmentConfig();

    // Can be used by RXJava to detect when the fragment is paused.
    protected PublishSubject<Void> paused = PublishSubject.create();

    /**
     * Auto-inflate the fragment view based
     * on standard naming convention.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Fetch the content view of the fragment.
        view = MGLifecycleContentView.getContentView(this, container, view);

        onCreateView(savedInstanceState, view);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Emit pause event.
        paused.onNext(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset butter-knife injections.
        ButterKnife.reset(this);
    }

    /**
     * Shorthand for on create view.  The view
     * has already been created at this point.
     */
    public void onCreateView(Bundle savedInstanceState, View view) {

        // Enable butter-knife injections.
        ButterKnife.inject(this, view);

        // If saved state this is not the first creation.
        getConfig().setRecreated(savedInstanceState != null);
    }

    /**
     * Adds a clear history overload to the
     * start activity call.
     */
    public void startActivity(Intent intent, boolean clearHistory) {
        super.startActivity(intent);

        if (clearHistory) {

            ((MGLifecycleActivity)getActivity()).getConfig().setHistoryCleared();
        }
    }

    /**
     * Start activity without any additional
     * intent options and clear history flag.
     */
    public void startActivity(Class activityClass, boolean clearHistory) {
        Intent intent = new Intent(getActivity(), activityClass);

        startActivity(intent, clearHistory);
    }

    /**
     * Start activity without any additional
     * intent options.
     */
    public void startActivity(Class activityClass) {
        startActivity(activityClass, false);
    }
}