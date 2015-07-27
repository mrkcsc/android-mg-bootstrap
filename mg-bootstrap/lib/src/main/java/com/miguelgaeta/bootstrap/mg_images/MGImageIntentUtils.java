package com.miguelgaeta.bootstrap.mg_images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import com.miguelgaeta.bootstrap.mg_log.MGLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.NonNull;

/**
 * Project: https://github.com/iPaulPro/aFileChooser
 */
@SuppressWarnings("UnusedDeclaration")
class MGImageIntentUtils {

    private static final String imageFolderName = "intent_handler_images";

    public static File createTempImageFile() {

        return createTempImageFile(imageFolderName);
    }

    /**
     * Given a file try to infer the amount of
     * rotation needed so it is correctly oriented.
     */
    public static int getRotationDegree(@NonNull String imagePath) {

        try {

            ExifInterface ei = new ExifInterface(imagePath);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {

                case ExifInterface.ORIENTATION_NORMAL:
                case ExifInterface.ORIENTATION_UNDEFINED:
                    return 0;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }

        } catch (Exception ignored) { }

        return 0;
    }

    public static File getResizedImage(@NonNull File originalFile, int maxWidth) {

        try {

            Bitmap originalBitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());

            int width = originalBitmap.getWidth() > maxWidth ? maxWidth : originalBitmap.getWidth();

            int height = (int)(originalBitmap.getHeight() * (width / (float)originalBitmap.getWidth()));

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidth, height, false);

            return saveFile(originalBitmap, resizedBitmap);

        } catch (Exception e) {

            MGLog.i(e, "Unable to resize image.");
        }

        return originalFile;
    }

    public static File getRotatedImage(@NonNull File originalFile, int degree) {

        if (degree > 0) {

            try {

                Bitmap originalBitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());

                Matrix matrix = new Matrix();

                matrix.setRotate(degree);

                Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

                return saveFile(originalBitmap, rotatedBitmap);

            } catch (Exception e) {

                MGLog.i(e, "Unable to rotate image.");
            }
        }

        return originalFile;
    }

    /**
     * Save bitmap to disk and close all
     * streams and outputs.
     */
    private static File saveFile(@NonNull Bitmap bitmapOld, @NonNull Bitmap bitmapNew) throws IOException {

        File file = createTempImageFile();

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        bitmapNew.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

        fileOutputStream.flush();
        fileOutputStream.close();

        bitmapOld.recycle();
        bitmapNew.recycle();

        return file;
    }

    /**
     * Create a temporary image file in the device
     * external storage directory.
     */
    private static File createTempImageFile(String folderInExternalDirectory) {

        String imageFileName = "IMG_" + System.currentTimeMillis();

        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + folderInExternalDirectory);

        if (!dir.exists()) {

            boolean result = dir.mkdirs();
        }

        File image = null;

        try {

            image = File.createTempFile(imageFileName, ".jpg", dir);

        } catch (Exception ignored) { }

        return image;
    }
}
