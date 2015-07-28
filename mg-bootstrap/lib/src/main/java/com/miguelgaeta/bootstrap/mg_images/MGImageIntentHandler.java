package com.miguelgaeta.bootstrap.mg_images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.android.camera.CropImageIntentBuilder;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 7/20/15.
 */
@SuppressWarnings("UnusedDeclaration") @NoArgsConstructor(staticName = "create")
public class MGImageIntentHandler {

    public static final int REQUEST_CAPTURE = 777;
    public static final int REQUEST_GALLERY = 779;
    public static final int REQUEST_CROP    = 800;

    private static Uri fileUri;

    public static void startForImageCapture(@NonNull FragmentActivity activity, Action1<Void> onError) {

        startForIntent(activity, null, file -> new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)), REQUEST_CAPTURE, onError);
    }

    public static void startForImageCapture(@NonNull Fragment fragment, Action1<Void> onError) {

        startForIntent(null, fragment, file -> new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)), REQUEST_CAPTURE, onError);
    }

    public static void startForImagePick(@NonNull FragmentActivity activity) {

        startActivityForResult(activity, null, new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_GALLERY);
    }

    public static void startForImagePick(@NonNull Fragment fragment) {

        startActivityForResult(null, fragment, new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_GALLERY);
    }

    public static void startForImageCrop(@NonNull Fragment fragment, @NonNull Uri uri, @ColorRes int colorResId, Action1<Void> onError) {

        startForIntent(null, fragment, file -> {

            CropImageIntentBuilder intentBuilder = new CropImageIntentBuilder(128, 128, Uri.fromFile(file));

            int color = fragment.getResources().getColor(colorResId);

            intentBuilder.setSourceImage(uri);
            intentBuilder.setDoFaceDetection(true);
            intentBuilder.setOutlineCircleColor(color);
            intentBuilder.setOutlineColor(color);
            intentBuilder.setScaleUpIfNeeded(true);

            return intentBuilder.getIntent(fragment.getActivity());

        }, REQUEST_CROP, onError);
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

            if (resultCode == Activity.RESULT_OK) {

                switch (requestCode) {
                    
                    case REQUEST_CAPTURE:
                    case REQUEST_CROP:

                        uri = fileUri;

                        break;

                    case REQUEST_GALLERY:

                        if (data != null) {

                            uri = data.getData();
                        }

                        break;
                }
            }

            if (uri != null) {

                File file = MGImagePathUtil.getFile(context, uri);

                if (file != null && file.exists()) {

                    int rotation = MGImageIntentUtils.getRotationDegree(file.getAbsolutePath());

                    file = MGImageIntentUtils.getResizedImage(file, 1080);
                    file = MGImageIntentUtils.getRotatedImage(file, rotation);
                    
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

    private static void startForIntent(FragmentActivity activity, Fragment fragment, @NonNull Func1<File, Intent> onIntent, int requestCode, Action1<Void> onError) {

        File file = MGImageIntentUtils.createTempImageFile();

        fileUri = Uri.fromFile(file);

        if ((file != null) && file.exists()) {

            startActivityForResult(activity, fragment, onIntent.call(file), requestCode);

        } else if (onError != null) {

            onError.call(null);
        }
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
