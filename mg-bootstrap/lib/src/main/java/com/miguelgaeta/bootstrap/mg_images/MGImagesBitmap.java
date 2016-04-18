package com.miguelgaeta.bootstrap.mg_images;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.concurrent.Executor;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 9/29/15.
 */
@lombok.Builder @SuppressWarnings("unused")
public class MGImagesBitmap {

    private boolean circle;

    private String url;

    private  int width;

    private int height;

    private Executor executor;

    /**
     * Convenience method.
     */
    public static MGImagesBitmap create(@NonNull String url, int width, int height, boolean circle, final boolean currentThread) {
        return
            MGImagesBitmap
                .builder()
                .url(url)
                .width(width)
                .height(height)
                .circle(circle)
                .executor(new Executor() {
                    @Override
                    public void execute(@NonNull final Runnable command) {
                        if (currentThread) {
                            command.run();
                        } else {
                            new DefaultExecutorSupplier(1).forDecode();
                        }
                    }
                })
                .build();
    }

    /**
     * Fetch bitmap.
     */
    public void getBitmap(@NonNull final Action1<Bitmap> callback) {

        // Get image request.
        ImageRequestBuilder request = MGImages.getImageRequest(url, width, height);

        if (circle) {

            request.setPostprocessor(new CirclePostProcessor(width, height));
        }

        // Create a data source to decode the target image.
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(request.build(), null);

        // Guaranteed to return a bitmap or null.
        dataSource.subscribe(new BaseBitmapDataSubscriber() {

            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {

                callback.call(bitmap);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

                callback.call(null);
            }

        }, executor);
    }

    /**
     * Fetch bitmap as observable.
     */
    public Observable<Bitmap> getBitmapObservable() {

        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(final Subscriber<? super Bitmap> subscriber) {
                getBitmap(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    /**
     * Creates an actual circular bitmap
     * from a source bitmap.
     *
     * Source: http://bit.ly/1lCdkbB
     */
    private static class CirclePostProcessor extends BasePostprocessor {

        final int width;
        final int height;

        public CirclePostProcessor(int width, int height) {

            this.width = width;
            this.height = height;
        }

        public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {

            CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(width, height);

            try {
                Bitmap destBitmap = bitmapRef.get();

                Canvas canvas = new Canvas(destBitmap);

                final int color = 0xff424242;
                final Paint paint = new Paint();

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);

                canvas.drawCircle(width / 2, height / 2, width / 2, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
                    new Rect(0, 0, width, height), paint);

                return CloseableReference.cloneOrNull(bitmapRef);
            } finally {
                CloseableReference.closeSafely(bitmapRef);
            }
        }
    }
}
