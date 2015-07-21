package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_view.MGViewOnPressListener;

import butterknife.ButterKnife;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGRecyclerViewHolder<T extends MGRecyclerAdapter> extends RecyclerView.ViewHolder {

    @Getter(AccessLevel.PROTECTED)
    private final T adapter;

    protected interface OnClickAction {

        void onClick(View view, int position);
    }

    protected interface OnPressAction {

        void onPress(View view, int position, boolean pressed);
    }

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
        ButterKnife.bind(this, itemView);
    }

    /**
     * Short hand that auto inflated the resource.
     */
    public MGRecyclerViewHolder(@LayoutRes int layout, T adapter) {

        this(LayoutInflater.from(adapter.getRecycler().getContext()).inflate(layout, adapter.getRecycler(), false), adapter);
    }

    /**
     * Simulated the one create method
     * of fragments and activities.
     */
    protected void onConfigure(int position) {

    }

    protected void onClick(@NonNull View view, @NonNull OnClickAction action) {

        view.setOnClickListener(clickedView -> {

            if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                action.onClick(clickedView, getAdapterPosition());
            }
        });
    }

    protected void onClick(@NonNull OnClickAction action) {

        if (itemView != null) {

            onClick(itemView, action);
        }
    }

    protected void onPress(@NonNull View view, @NonNull OnPressAction action) {

        view.setOnTouchListener(new MGViewOnPressListener() {

            @Override
            public void onPress(boolean pressed) {

                if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                    action.onPress(view, getAdapterPosition(), pressed);
                }
            }
        });
    }

    protected void onPress(@NonNull OnPressAction action) {

        if (itemView != null) {

            onPress(itemView, action);
        }
    }
}
