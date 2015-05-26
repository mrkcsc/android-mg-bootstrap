package com.miguelgaeta.bootstrap.mg_reflection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGReflection {

    @Getter(lazy = true)
    private static final MGReflectionConfig config = new MGReflectionConfig();

    /**
     * Given a resource name as a string, fetch the associated
     * resource integer id from the target resource class -
     * layout, drawable, strings, etc.
     */
    public static Integer getResourceId(@NonNull String resourceName, @NonNull Class<?> resourceClass) {

        // Fetch resources.
        Resources resources = getConfig().getContext().getResources();

        // Fetch application package.
        String applicationPackageName = getConfig().getContext().getPackageName();

        // Try to fetch associated resource id.
        int resourceId = resources.getIdentifier(resourceName, resourceClass.getSimpleName(), applicationPackageName);

        return resourceId == 0 ? null : resourceId;
    }

    /**
     * Fetch integer resource.  Uses application context
     * to allow for decoding of integer resource from
     * any context.
     */
    public static Integer getInteger(int integerResourceId) {

        return getConfig().getContext().getResources().getInteger(integerResourceId);
    }

    /**
     * Convert density independent pixels to the associated
     * pixel value based on the screen size and density.
     */
    public static int dipToPixels(float densityPixels) {

        Resources resources = getConfig().getContext().getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                densityPixels, resources.getDisplayMetrics()));
    }

    /**
     * Given a context, fetch the size of this
     * devices screen.
     */
    public static Point getScreenSize() {

        Point point = new Point();

        WindowManager windowManager = (WindowManager)getConfig().getContext().getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getSize(point);

        return point;
    }

    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     * @param context Used to find the correct file
     * @param fieldName The name of the field-to-access
     * @return The value of the field, or {@code null} if the field is not found.
     */
    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {

            // Fetch app build config class.
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");

            // Fetch associated field.
            return clazz.getField(fieldName).get(null);

        } catch (Exception e) {

            return null;
        }
    }
}
