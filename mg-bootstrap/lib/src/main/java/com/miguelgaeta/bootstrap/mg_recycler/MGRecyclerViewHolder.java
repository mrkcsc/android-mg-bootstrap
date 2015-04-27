package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGRecyclerViewHolder<T extends MGRecyclerAdapter> extends RecyclerView.ViewHolder {

    @Getter(AccessLevel.PROTECTED)
    private final T adapter;

    @Getter(AccessLevel.PRIVATE)
    private final SerializedSubject<Void, Void> pausedHolder = new SerializedSubject<>(PublishSubject.create());

    private Subscription resumedSubscription;

    /**
     * This recycler view holder subclass exposes
     * the associated adapter so it can self
     * configure itself.
     */
    public MGRecyclerViewHolder(View itemView, T adapter) {
        super(itemView);

        // Set the adapter.
        this.adapter = adapter;

        // Enable butter knife.
        ButterKnife.inject(this, itemView);
    }

    /**
     * Simulated the one create method
     * of fragments and activities.
     */
    protected void onCreate(int position) {

    }

    /**
     * Simulates the on resume method of
     * fragments and activities.
     */
    protected void onResume(int position) {

        // Kill resume subscription.
        if (resumedSubscription != null) {
            resumedSubscription.unsubscribe();
        }

        // Set it up again.
        resumedSubscription = getAdapter().getResumed().observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {

            onResume(position);
        });

        // Emit pause
        getAdapter().getPaused().takeUntil(getPaused()).observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {

            getPausedHolder().onNext(null);
        });
    }

    /**
     * Simulates the on pause method of
     * fragments and activities.
     */
    protected void onPause() {

        // Kill resume subscription.
        if (resumedSubscription != null) {
            resumedSubscription.unsubscribe();
        }

        // Or if paused called directly.
        getPausedHolder().onNext(null);
    }

    /**
     * Gets the paused stream as
     * an observable.
     */
    protected final Observable<Void> getPaused() {

        return getPausedHolder().asObservable();
    }
}
