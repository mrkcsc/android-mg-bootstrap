package com.miguelgaeta.bootstrap.mg_images;

import com.facebook.drawee.drawable.ScalingUtils;

/**
 * Created by Miguel Gaeta on 6/23/15.
 */
public enum MGImagesScaleType {

    FIT_XY,
    FIT_START,
    FIT_CENTER,
    FIT_END,
    CENTER,
    CENTER_INSIDE,
    CENTER_CROP,
    FOCUS_CROP;

    public static ScalingUtils.ScaleType from(MGImagesScaleType scaleType) {

        switch (scaleType) {

            case FIT_XY:        return ScalingUtils.ScaleType.FIT_XY;
            case FIT_START:     return ScalingUtils.ScaleType.FIT_START;
            case FIT_CENTER:    return ScalingUtils.ScaleType.FIT_CENTER;
            case FIT_END:       return ScalingUtils.ScaleType.FIT_END;
            case CENTER:        return ScalingUtils.ScaleType.CENTER;
            case CENTER_INSIDE: return ScalingUtils.ScaleType.CENTER_INSIDE;
            case CENTER_CROP:   return ScalingUtils.ScaleType.CENTER_CROP;
            case FOCUS_CROP:    return ScalingUtils.ScaleType.FOCUS_CROP;

        }

        return ScalingUtils.ScaleType.CENTER_CROP;
    }
}
