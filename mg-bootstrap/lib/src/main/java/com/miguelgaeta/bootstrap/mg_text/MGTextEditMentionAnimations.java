package com.miguelgaeta.bootstrap.mg_text;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by mrkcsc on 5/24/15.
 */
class MGTextEditMentionAnimations extends Animation {

    protected final int originalHeight;
    protected final View view;
    protected float perValue;

    private MGTextEditMentionAnimations(View view, int fromHeight, int toHeight) {
        this.view = view;
        this.originalHeight = fromHeight;
        this.perValue = (toHeight - fromHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = (int) (originalHeight + perValue * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    /**
     * Create and run animation.
     */
    public static void create(RecyclerView recyclerView, int heightOld, int heightNew) {

        // Set the visibility.
        recyclerView.setVisibility(heightNew > 0 ? View.VISIBLE : View.GONE);

        // Create the animation.
        Animation animation = new MGTextEditMentionAnimations(recyclerView, heightOld, heightNew);

        // Set a modest duration.
        animation.setDuration(100);

        // Lets do it.
        recyclerView.startAnimation(animation);
    }
}
