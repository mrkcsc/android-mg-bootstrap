package com.miguelgaeta.bootstrap.mg_images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 7/20/15.
 */
@SuppressWarnings("UnusedDeclaration")
class MGImageIntentUtils {

    public static File createImageFile(String folderInExternalDirectory) {

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

    public static Bitmap getBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0)
            return 1;

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static File getSmallImageFromSDCard(String folderInExternalDirectory, @NonNull File originalFile, int width) {

        try {

            Bitmap originalBitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());

            if (originalBitmap.getWidth() > width) {

                int height = (int)(originalBitmap.getHeight() * (width / (float)originalBitmap.getWidth()));

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);

                File file = createImageFile(folderInExternalDirectory);

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

                originalBitmap.recycle();
                resizedBitmap.recycle();

                return file;
            }

        } catch (Exception ignored) { }

        return originalFile;
    }

    public static String getRightAngleImage(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree;
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree, photoPath);

        } catch (Exception ignored) { }

        return photoPath;
    }

    public static String rotateImage(int degree, String imagePath) {

        if (degree <= 0) {
            return imagePath;
        }
        try {
            Bitmap b = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if (b.getWidth() > b.getHeight()) {
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                    matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 80, out);
            } else if (imageType.equalsIgnoreCase("jpeg") || imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();

        } catch (Exception ignored) { }

        return imagePath;
    }
}
