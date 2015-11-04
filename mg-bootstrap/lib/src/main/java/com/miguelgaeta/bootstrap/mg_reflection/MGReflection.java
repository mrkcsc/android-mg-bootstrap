package com.miguelgaeta.bootstrap.mg_reflection;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;

import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;

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
     * Fetch string resource.
     */
    public static String getString(@StringRes int resId) {

        return MGLifecycleApplication.getContext().getResources().getString(resId);
    }

    public static String getPackageName(@NonNull Context context) {

        return context.getPackageName();
    }

    /**
     * Given a resource name as a string, fetch the associated
     * resource integer id from the target resource class -
     * layout, drawable, strings, etc.
     */
    public static Integer getResourceId(@NonNull Context context, @NonNull String resourceName, @NonNull Class<?> resourceClass) {

        // Fetch application package.
        String applicationPackageName = context.getPackageName();

        // Try to fetch associated resource id.
        int resourceId = context.getResources().getIdentifier(resourceName, resourceClass.getSimpleName(), applicationPackageName);

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

        return (int) (densityPixels * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pixelsToDip(int pixels) {

        return (int) (pixels / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenWidth() {

        return dipToPixels(getScreenWidthDp());
    }

    public static int getScreenWidthDp() {

        return Resources.getSystem().getConfiguration().screenWidthDp;
    }

    public static int getScreenHeight() {

        return dipToPixels(getScreenHeightDp());
    }

    public static int getScreenHeightDp() {

        return Resources.getSystem().getConfiguration().screenHeightDp;
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
