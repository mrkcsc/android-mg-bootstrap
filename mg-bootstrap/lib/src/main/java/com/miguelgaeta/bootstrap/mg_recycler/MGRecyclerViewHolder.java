package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.miguelgaeta.bootstrap.mg_view.MGViewOnPressListener;

import java.util.Arrays;

import butterknife.ButterKnife;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 4/9/15.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal" })
public abstract class MGRecyclerViewHolder<T extends MGRecyclerAdapter> extends RecyclerView.ViewHolder {

    @Getter
    private final T adapter;

    public interface OnClick {

        void onClick(View view, int position);
    }

    public void onClick(@NonNull OnClick action, View... views) {

        ButterKnife.apply(Arrays.asList(views), (view, i) -> {

            if (view != null) {
                view.setOnClickListener(v -> {

                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                        action.onClick(v, getAdapterPosition());
                    }
                });
            }
        });
    }

    public void onClick(@NonNull OnClick action) {

        onClick(action, itemView);
    }

    public interface OnPressAction {

        void onPress(View view, int position, boolean pressed);
    }

    public void onPress(@NonNull OnPressAction action, View... views) {

        ButterKnife.apply(Arrays.asList(views), (view, i) -> {

            if (view != null) {
                view.setOnTouchListener(new MGViewOnPressListener() {

                    @Override
                    public void onPress(boolean pressed) {

                        if (getAdapterPosition() != RecyclerView.NO_POSITION) {

                            action.onPress(view, getAdapterPosition(), pressed);
                        }
                    }
                });
            }
        });
    }

    public void onPress(@NonNull OnPressAction action) {

        onPress(action, itemView);
    }

    public interface OnLongPress {

        boolean onLongClick(View view, int position);
    }

    public void onLongPress(OnLongPress action, View... views) {

        ButterKnife.apply(Arrays.asList(views), (view, i) -> {

            if (view != null) {
                view.setOnLongClickListener(v -> getAdapterPosition() != RecyclerView.NO_POSITION && action.onLongClick(v, getAdapterPosition()));
            }
        });
    }

    public void onLongPress(OnLongPress action) {

        onLongPress(action, itemView);
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
}
