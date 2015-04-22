package com.miguelgaeta.bootstrap.mg_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGViewRecyclerHolder<T extends MGRecyclerAdapter> extends RecyclerView.ViewHolder {

    @Getter(AccessLevel.PROTECTED)
    private final T adapter;

    @Getter(AccessLevel.PRIVATE)
    private final PublishSubject<Void> pausedHolder = PublishSubject.create();

    @Getter(AccessLevel.PRIVATE)
    private final Observable<Void> pausedAdapter;

    /**
     * This recycler view holder subclass exposes
     * the associated adapter so it can self
     * configure itself.
     */
    public MGViewRecyclerHolder(View itemView, T adapter) {
        super(itemView);

        // Set the adapter.
        this.adapter = adapter;

        // Set adapter paused observable.
        this.pausedAdapter = adapter.getPaused();

        // Enable butter knife.
        ButterKnife.inject(this, itemView);
    }

    /**
     * Simulates the on resume method of
     * fragments and activities.
     */
    protected void onResume() {

        // Emit pause if the adapter paused.
        getPausedAdapter().takeUntil(getPaused()).subscribe(r -> {

            getPausedHolder().onNext(null);
        });
    }

    /**
     * Simulates the on pause method of
     * fragments and activities.
     */
    protected void onPause() {

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

    /**
     * Configuration method for the view holder.  This method
     * is enforced for easy visibility.
     */
    public abstract void configure(int position);
}
