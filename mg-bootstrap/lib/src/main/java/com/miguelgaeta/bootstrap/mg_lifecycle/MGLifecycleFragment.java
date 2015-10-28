package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lombok.Getter;
import lombok.NonNull;
import rx.functions.Func0;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by mrkcsc on 12/2/14.
 */
@SuppressWarnings("unused")
public class MGLifecycleFragment extends Fragment implements MGLifecycleFragmentInterface {

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
        view = MGLifecycleContentView.getContentView(this, inflater, container, view);

        onCreateView(savedInstanceState, view);

        // Invoke create or resume.
        onCreateOrResume();

        // Create version has been invoked.
        getConfig().setOnCreateOrResumeInvoked(true);

        return view;
    }

    @Override
    public void onCreateOrResume() {

    }

    @Override
    public void onCreateView(Bundle savedInstanceState, View view) {

        getConfig().onCreateView(this, savedInstanceState, view);
    }

    @Override
    public void onResume() {
        super.onResume();

        getConfig().onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPaused().onNext(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getConfig().onDestroyView(this);
    }

    /**
     * Allows fragments to hook into the parent activity lifecycle
     * functionality. If the default back behavior is to be
     * suppressed, return true, otherwise return false.
     */
    public void setOnBackPressed(@NonNull String fragmentId, @NonNull Func0<Boolean> onBackAction) {

        ((MGLifecycleActivity) getActivity()).getFragmentOnBackPressed().put(fragmentId, onBackAction);
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