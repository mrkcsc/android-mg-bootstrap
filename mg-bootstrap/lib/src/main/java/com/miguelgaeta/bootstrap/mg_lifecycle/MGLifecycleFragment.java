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
import rx.subjects.SerializedSubject;

/**
 * Created by mrkcsc on 12/2/14.
 */
@SuppressWarnings("unused")
public class MGLifecycleFragment extends Fragment {

    @Getter
    // Configuration object for the fragment.
    private MGLifecycleFragmentConfig config = new MGLifecycleFragmentConfig();

    @Getter
    // Custom observable that emits activity paused events.
    private final SerializedSubject<Void, Void> paused = new SerializedSubject<>(PublishSubject.create());

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

        // Invoke create or resume.
        onCreateOrResume();

        // Create version has been invoked.
        getConfig().setOnCreateOrResumeInvoked(true);

        return view;
    }

    /**
     * Helper lifecycle method that runs either in onCreate or onResume.
     */
    protected void onCreateOrResume() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getConfig().isOnCreateOrResumeInvoked()) {
            getConfig().setOnCreateOrResumeInvoked(false);
        } else {

            // On resume version invoked.
            onCreateOrResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        getPaused().onNext(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset butter-knife injections.
        ButterKnife.unbind(this);
    }

    /**
     * Shorthand for on create view.  The view
     * has already been created at this point.
     */
    public void onCreateView(Bundle savedInstanceState, View view) {

        // Enable butter-knife injections.
        ButterKnife.bind(this, view);

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