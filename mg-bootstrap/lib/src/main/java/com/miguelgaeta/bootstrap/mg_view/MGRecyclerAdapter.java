package com.miguelgaeta.bootstrap.mg_view;

import android.support.v7.widget.RecyclerView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 4/21/15.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class MGRecyclerAdapter extends RecyclerView.Adapter<MGViewRecyclerHolder> {

    @Getter(AccessLevel.PROTECTED)
    private RecyclerView recycler;

    @Getter(AccessLevel.PROTECTED)
    private final Observable<Void> paused;

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.  In addition it exposts lifecycle
     * methods to the view holder cells which allow for
     * much easier mutation.
     */
    public MGRecyclerAdapter(@NonNull RecyclerView recycler, @NonNull Observable<Void> paused) {

        // Set recycler view.
        this.recycler = recycler;

        // Set on paused event.
        this.paused = paused;
    }

    /**
     * Invoke the lifecycle pause callback
     * on the associated view holder.
     */
    @Override
    public void onViewDetachedFromWindow(MGViewRecyclerHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.onPause();
    }

    /**
     * Automatically configure the view holder
     * with the specified item at position.
     */
    @Override
    public void onBindViewHolder(MGViewRecyclerHolder holder, int position) {

        holder.onResume(position);
    }
}
