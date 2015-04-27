package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 4/21/15.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class MGRecyclerAdapter extends RecyclerView.Adapter<MGRecyclerViewHolder> {

    @Getter(AccessLevel.PROTECTED)
    private final RecyclerView recycler;

    @Getter(AccessLevel.PROTECTED)
    private final Observable<Void> paused;

    @Getter(AccessLevel.PROTECTED)
    private final Observable<Void> resumed;

    private WeakReference<Map<String, MGRecyclerViewHolder>> sa;

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.  In addition it exposes lifecycle
     * methods to the view holder cells which allow for
     * much easier mutation.
    */
    public MGRecyclerAdapter(@NonNull RecyclerView recycler, @NonNull Observable<Void> paused, @NonNull Observable<Void> resumed) {

        // Set recycler view.
        this.recycler = recycler;

        // Set on paused event.
        this.paused = paused;

        // Set on resumed event.
        this.resumed = resumed;
    }

    /**
     * Invoke the lifecycle pause callback
     * on the associated view holder.
     */
    @Override
    public void onViewDetachedFromWindow(MGRecyclerViewHolder holder) {

        // Pause immediately.
        holder.onPause();

        super.onViewDetachedFromWindow(holder);
    }

    /**
     * Automatically configure the view holder
     * with the specified item at position.
     */
    @Override
    public void onBindViewHolder(MGRecyclerViewHolder holder, int position) {

        // Created.
        holder.onCreate(position);

        // Then resumed.
        holder.onResume(position);
    }
}
