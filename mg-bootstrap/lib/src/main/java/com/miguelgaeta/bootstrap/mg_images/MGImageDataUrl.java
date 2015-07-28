package com.miguelgaeta.bootstrap.mg_images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import lombok.NonNull;
import retrofit.mime.TypedFile;

/**
 * Created by Miguel Gaeta on 7/28/15.
 */
@SuppressWarnings("unused")
public class MGImageDataUrl {

    public static String toDataUrl(@NonNull Bitmap bitmap, @NonNull String mimeType) {

        return "data:" + mimeType + ";base64," + getEncodedData(bitmap);
    }

    public static String toDataUrl(@NonNull String filePath, @NonNull String mimeType) {

        return toDataUrl(BitmapFactory.decodeFile(filePath), mimeType);
    }

    public static String toDataUrl(@NonNull File file, @NonNull String mimeType) {

        return toDataUrl(file.getAbsolutePath(), mimeType);
    }

    public static String toDataUrl(@NonNull TypedFile typedFile) {

        return toDataUrl(typedFile.file(), typedFile.mimeType());
    }

    private static String getEncodedData(@NonNull Bitmap bitmap) {

        ByteArrayOutputStream bitmapData = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapData);

        String encodedData = Base64.encodeToString(bitmapData.toByteArray(), Base64.NO_WRAP);

        bitmap.recycle();

        try {

            bitmapData.close();

        } catch (IOException ignored) { }

        return encodedData;
    }
}
