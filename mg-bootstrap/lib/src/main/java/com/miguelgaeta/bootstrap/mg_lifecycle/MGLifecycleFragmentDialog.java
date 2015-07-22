package com.miguelgaeta.bootstrap.mg_lifecycle;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import lombok.Getter;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Miguel Gaeta on 7/21/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLifecycleFragmentDialog extends DialogFragment implements MGLifecycleFragmentInterface {

    @Getter(lazy = true)
    private final MGLifecycleFragmentConfig config = new MGLifecycleFragmentConfig();

    @Getter
    private final SerializedSubject<Void, Void> paused = new SerializedSubject<>(PublishSubject.create());

    /**
     * Create a standard dialog by inferring the
     * desired content view.
     */
    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Integer contentViewResourceId = MGLifecycleContentView.getContentView(this.getClass());

        if (contentViewResourceId != null) {

            View view = getActivity().getLayoutInflater().inflate(contentViewResourceId, null);

            onCreateView(savedInstanceState, view);

            // Invoke create or resume.
            onCreateOrResume();

            // Create version has been invoked.
            getConfig().setOnCreateOrResumeInvoked(true);

            builder.setView(view);
        }

        builder = onConfigureBuilder(savedInstanceState, builder);

        return builder.create();
    }

    @Override
    public void onCreateView(Bundle savedInstanceState, View view) {

        getConfig().onCreateView(savedInstanceState, view);
    }

    @Override
    public void onCreateOrResume() {

    }

    @Override
    public void onPause() {
        super.onPause();

        getPaused().onNext(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        getConfig().onResume(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getConfig().onDestroyView();
    }

    /**
     * Allow the dialog fragment callee to perform
     * additional configurations to the builder.
     */
    public AlertDialog.Builder onConfigureBuilder(Bundle savedInstanceState, AlertDialog.Builder builder) {

        return builder;
    }
}
