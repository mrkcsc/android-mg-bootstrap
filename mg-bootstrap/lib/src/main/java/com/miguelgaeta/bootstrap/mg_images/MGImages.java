package com.miguelgaeta.bootstrap.mg_images;

import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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

    /**
     * Gets an image request with some commonly used parameters
     * such as the desired width and height.
     */
    static ImageRequestBuilder getImageRequest(String url, int width, int height) {

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
}
