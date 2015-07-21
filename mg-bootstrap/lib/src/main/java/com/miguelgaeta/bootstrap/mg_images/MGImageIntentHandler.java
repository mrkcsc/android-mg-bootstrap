package com.miguelgaeta.bootstrap.mg_images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.miguelgaeta.bootstrap.mg_reflection.MGReflection;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Miguel Gaeta on 7/20/15.
 */
@SuppressWarnings("UnusedDeclaration") @NoArgsConstructor(staticName = "create")
public class MGImageIntentHandler {

    private static final int REQUEST_CAPTURE = 100;
    private static final int REQUEST_GALLERY = 200;

    private int mWidth = 120;
    private int mHeight = 120;

    private static final String folderName = "intent_handler";

    private static Uri fileUri;

    public static void startForImageCapture(@NonNull FragmentActivity activity, Action1<Void> onError) {

        startForImageCapture(activity, null, onError);
    }

    public static void startForImageCapture(@NonNull Fragment fragment, Action1<Void> onError) {

        startForImageCapture(null, fragment, onError);
    }

    private static void startForImageCapture(FragmentActivity activity, Fragment fragment, Action1<Void> onError) {

        File file = MGImageIntentUtils.createImageFile(folderName);

        fileUri = Uri.fromFile(file);

        if ((file != null) && file.exists()) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

            startActivityForResult(activity, fragment, intent, REQUEST_CAPTURE);

        } else if (onError != null) {

            onError.call(null);
        }
    }

    public static void startForImagePick(@NonNull FragmentActivity activity) {

        startActivityForResult(activity, null, new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_GALLERY);
    }

    public static void startForImagePick(@NonNull Fragment fragment) {

        startActivityForResult(null, fragment, new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_GALLERY);
    }

    private static void startActivityForResult(FragmentActivity activity, Fragment fragment, @NonNull Intent intent, int requestCode) {

        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);

        } else

        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    public Observable<FileResult> handleResult(@NonNull Context context, int requestCode, int resultCode, Intent data) {

        return Observable.create(subscriber -> {

            Uri uri = null;

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAPTURE) {

                uri = fileUri;

            } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY && data != null) {

                uri = data.getData();
            }

            if (uri != null) {

                File file = MGImagePathUtil.getFile(context, uri);

                if (file != null && file.exists()) {
                    file = MGImageIntentUtils.getSmallImageFromSDCard(folderName, file, MGReflection.getScreenWidth());
                    
                    subscriber.onNext(FileResult.create(new TypedFile(MGImagePathUtil.getMimeType(file), file), FileResult.Status.OK));

                } else {

                    subscriber.onNext(FileResult.create(null, FileResult.Status.FILE_NOT_FOUND));
                }

            } else  {

                subscriber.onNext(FileResult.create(null, FileResult.Status.URI_NOT_FOUND));
            }

            subscriber.onCompleted();
        });
    }

    @AllArgsConstructor(staticName = "create") @Getter
    public static class FileResult {

        public enum Status {

            OK, FILE_NOT_FOUND, URI_NOT_FOUND
        }

        private TypedFile typedFile;

        private @NonNull Status status;
    }
}
