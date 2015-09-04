package com.miguelgaeta.bootstrap.mg_images;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.DefaultExecutorSupplier;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import rx.Observable;

/**
 * Created by Miguel Gaeta on 6/23/15.
 */
@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class MGImages {

    private static final int SMALL_IMAGE_MAX_SIZE = 200;

    public static void setPlaceholderImage(ImageView view, int resourceId) {

        getHierarchy(view).setPlaceholderImage(resourceId);
    }

    public static void setScaleType(ImageView view, MGImagesScaleType scaleType) {

        getHierarchy(view).setActualImageScaleType(MGImagesScaleType.from(scaleType));
    }

    public static void setCornerRadius(ImageView view, float cornerRadius, boolean circle) {

        getHierarchy(view).setRoundingParams(circle ? RoundingParams.asCircle() : RoundingParams.fromCornersRadius(cornerRadius));
    }

    public static void setImage(ImageView view, int resourceId) {

        getHierarchy(view).setPlaceholderImage(resourceId);
    }

    public static void setImage(ImageView view, String url) {

        setImage(view, url, 0, 0);
    }

    public static void setImage(ImageView view, String url, int width, int height) {

        // Create URI.
        Uri uri = Uri.parse(url);

        // Create an image controller builder.
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();

        // Provide some standard config.
        builder = builder.setOldController(getDrawee(view).getController()).setUri(uri).setAutoPlayAnimations(true);

        // Get image request.
        ImageRequestBuilder request = getImageRequest(url, width, height);

        // Generate the final controller with image request.
        getDrawee(view).setController(builder.setImageRequest(request.build()).build());
    }

    public static Observable<Bitmap> getBitmap(String url) {

        return getBitmap(url, 0, 0);
    }

    public static Observable<Bitmap> getBitmap(String url, int width, int height) {

        return getBitmap(url, width, height, false);
    }

    /**
     * Gets a raw bitmap using the fresco image pipeline.  It is not safe
     * to keep the bitmap around past the scope of the callback
     * or assign it outside of it's scope.  If you need that make
     * a complete copy of it.
     *
     * GCM is an exception because notification compat already make
     * a copy of any bitmaps it gets provided.  See:
     *
     * http://bit.ly/1FbwdFy
     */
    public static Observable<Bitmap> getBitmap(String url, int width, int height, boolean circle) {

        return Observable.create(subscriber -> {

            // Get image request.
            ImageRequestBuilder request = getImageRequest(url, width, height);

            if (circle) {

                request.setPostprocessor(new CirclePostProcessor(width, height));
            }

            // Create a data source to decode the target image.
            DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(request.build(), null);

            // Guaranteed to return a bitmap or null.
            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {

                    subscriber.onNext(bitmap);
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {

                    subscriber.onNext(null);
                }

            }, new DefaultExecutorSupplier(1).forDecode());
        });
    }

    /**
     * Gets an image request with some commonly used parameters
     * such as the desired width and height.
     */
    private static ImageRequestBuilder getImageRequest(String url, int width, int height) {

        // Create a resize image request, allow full cache lookups.
        ImageRequestBuilder request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH);

        boolean smallImage = !url.contains("gif") && width <= SMALL_IMAGE_MAX_SIZE && height <= SMALL_IMAGE_MAX_SIZE;

        // Use a smaller cache for everything else.
        request = request.setImageType(smallImage ? ImageRequest.ImageType.SMALL : ImageRequest.ImageType.DEFAULT);

        if (width > 0 && height > 0) {

            request = request.setResizeOptions(new ResizeOptions(width, height));
        }

        return request;
    }

    /**
     * Fetch fresco simple drawee object of a normal
     * image view - will crash if it is not.
     */
    private static DraweeView getDrawee(ImageView imageView) {

        return (DraweeView)imageView;
    }

    /**
     * Get the hierarchy of an assumed Fresco drawee.  Allows
     * us to do in-place modifications of the image.
     */
    private static GenericDraweeHierarchy getHierarchy(ImageView imageView) {

        if (!getDrawee(imageView).hasHierarchy()) {
             getDrawee(imageView).setHierarchy(new GenericDraweeHierarchyBuilder(imageView.getResources()).build());
        }

        return (GenericDraweeHierarchy)getDrawee(imageView).getHierarchy();
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
