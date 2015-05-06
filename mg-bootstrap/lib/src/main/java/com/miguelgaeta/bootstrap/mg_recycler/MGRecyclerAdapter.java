package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 4/21/15.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class MGRecyclerAdapter extends RecyclerView.Adapter<MGRecyclerViewHolder> {

    @Getter(AccessLevel.PROTECTED)
    private final RecyclerView recycler;

    /**
     * This adapter streamlines common recycler view operations
     * and boiler plate code.
    */
    public MGRecyclerAdapter(@NonNull RecyclerView recycler) {

        // Set recycler view.
        this.recycler = recycler;
    }

    /**
     * Configures a recycler view and creates an instance of the
     * adapter.  Configuration is directly coupled to this
     * particular application.
     */
    public static <T extends MGRecyclerAdapter> T configure(@NonNull RecyclerView recyclerView, @NonNull Class<T> adapterClass, @NonNull RecyclerView.LayoutManager layoutManager) {

        try {

            Class[] adapterConstructorArgs = new Class[1];

            adapterConstructorArgs[0] = RecyclerView.class;

            // Use the dark magic of generics to make an instance of the adapter.
            T adapter = adapterClass.getDeclaredConstructor(adapterConstructorArgs).newInstance(recyclerView);

            // Configure recycler view with standard config.
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.getItemAnimator().setSupportsChangeAnimations(false);

            // Return adapter.
            return adapter;

        } catch (Exception e) {

            throw new RuntimeException("Unable to instantiate adapter.");
        }
    }

    public static <T extends MGRecyclerAdapter> T configure(@NonNull RecyclerView recyclerView, @NonNull Class<T> adapterClass) {

        return configure(recyclerView, adapterClass, new LinearLayoutManager(recyclerView.getContext()));
    }

    /**
     * Automatically configure the view holder
     * with the specified item at position.
     */
    @Override
    public void onBindViewHolder(MGRecyclerViewHolder holder, int position) {

        // Configure.
        holder.onConfigure(position);
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount, int scrollToPosition, boolean smoothScroll) {

        notifyItemRangeRemoved(positionStart, itemCount);

        scrollToPosition(scrollToPosition, smoothScroll);
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount, int scrollToPosition) {

        notifyItemRangeRemoved(positionStart, itemCount, scrollToPosition, false);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount, int scrollToPosition, boolean smoothScroll) {

        notifyItemRangeInserted(positionStart, itemCount);

        scrollToPosition(scrollToPosition, smoothScroll);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount, int scrollToPosition) {

        notifyItemRangeInserted(positionStart, itemCount, scrollToPosition, false);
    }

    public void notifyItemRemoved(int position, int scrollToPosition, boolean smoothScroll) {

        notifyItemRemoved(position);

        scrollToPosition(scrollToPosition, smoothScroll);
    }

    public void notifyItemRemoved(int position, int scrollToPosition) {

        notifyItemRemoved(position, scrollToPosition, false);
    }

    public void notifyItemInserted(int position, int scrollToPosition, boolean smoothScroll) {

        notifyItemInserted(position);

        scrollToPosition(scrollToPosition, smoothScroll);
    }

    public void notifyItemInserted(int position, int scrollToPosition) {

        notifyItemInserted(position, scrollToPosition, false);
    }

    /**
     * Scroll to a position in the list.  Optionally
     * can specify if the scrolling should be animated.
     */
    public void scrollToPosition(int position, boolean smoothScroll) {

        if (smoothScroll) {

            // Smooth scroll baby.
            getRecycler().smoothScrollToPosition(position);

        } else {

            // Normal scroll.
            getRecycler().scrollToPosition(position);
        }
    }

    /**
     * Scroll to a position in the list.
     */
    public void scrollToPosition(int position) {

        scrollToPosition(position, false);
    }
}
