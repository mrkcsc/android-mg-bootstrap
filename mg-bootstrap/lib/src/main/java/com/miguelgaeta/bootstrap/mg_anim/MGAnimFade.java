package com.miguelgaeta.bootstrap.mg_anim;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.miguelgaeta.bootstrap.R;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by mrkcsc on 8/21/14.
 *
 * TODO: Preserve any existing alpha, allow setting alpha.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGAnimFade {

    /**
     * Set the visibility of a view with a fade animation.
     */
    public static Observable<Void> setVisibility(@NonNull View view, int visibility) {

        Observable<Void> observeViewSize;

        // Make the view initially visible.
        view.setVisibility(View.VISIBLE);

        switch (visibility) {
            case View.VISIBLE:
                observeViewSize = getAnimationObservable(view, R.anim.anim_fade_in);
                break;
            case View.INVISIBLE:
                observeViewSize = getAnimationObservable(view, R.anim.anim_fade_out);
                break;
            case View.GONE:
                observeViewSize = getAnimationObservable(view, R.anim.anim_fade_out);
                break;
            default:

                // Hard crash if a bad flag is provided.
                throw new RuntimeException("Invalid view visibility flag passed.");
        }

        // Set the final specified visibility.
        observeViewSize.subscribe(result -> view.setVisibility(visibility));

        return observeViewSize;
    }

    /**
     * Fetch an observable that is tied
     * to the on animation end event
     * of some animation resource.
     */
    private static Observable<Void> getAnimationObservable(@NonNull View view, int animationResourceId) {

        Observable<Void> animationObservable = Observable.create(subscriber -> {

            // Load the associated animation.
            Animation anim = AnimationUtils.loadAnimation(view.getContext(), animationResourceId);

            // Finish observable when complete.
            anim.setAnimationListener(new OnAnimationEnd() {

                @Override
                public void onAnimationEnd(Animation animation) {

                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
            });

            view.startAnimation(anim);
        });

        return animationObservable.observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Only exposes the onAnimationEnd callback.
     */
    private static abstract class OnAnimationEnd implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    }
}