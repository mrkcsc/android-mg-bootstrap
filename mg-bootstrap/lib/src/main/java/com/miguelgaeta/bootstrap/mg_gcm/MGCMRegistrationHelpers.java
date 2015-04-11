package com.miguelgaeta.bootstrap.mg_gcm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by mrkcsc on 8/6/14. Copyright 2014 Blitz Studios
 */
public class MGCMRegistrationHelpers {

    // region Member Variables
    // ============================================================================================================

    // Request tag for asking user to obtain google play services.
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // This is the project number obtained from the API Console.
    private static final String SENDER_ID = "294665201786";

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Attempt to register device for GCM messages. Will
     * run on background thread if needed.
     *
     * @param activity Activity context.
     *
     * @return Can fail if this device does not have or
     *         support google play services.
     */
    @SuppressWarnings("unused")
    public static boolean tryRegistration(Activity activity) {

        // Check device for Play Services APK.
        boolean hasPlayServices = checkPlayServices(activity);

        // If check succeeds, proceed with GCM registration.
        if (hasPlayServices) {

            // If there is no registration id available.
            if (getRegistrationId(activity) == null) {

                // Fetch it in background.
                registerInBackground(activity);

            } //else if (!AuthHelper.instance().isDeviceRegistered()) {

                // Try to persist if not persisted.
                //sendRegistrationIdToBackend(activity, getRegistrationId(activity));
            //}
        }

        return hasPlayServices;
    }

    /**
     * Attempt to fetch and store a unique device id. First
     * uses a standard approach to fetch device id.  If
     * that fails, it will generate a random GUID identifier
     * for the device.
     *
     * @param context Context.
     *
     * @return Device id.
     */
    @SuppressWarnings("unused")
    public static String getDeviceId(Context context) {

        // Attempt to fetch cached device id.
        String deviceId = null;// AppDataObject.gcmDeviceId.get();

        if (deviceId == null) {
            deviceId = Settings.Secure.getString
                    (context.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (deviceId == null) {
                deviceId = java.util.UUID.randomUUID().toString();
            }

            // Persist the device id.
            //AppDataObject.gcmDeviceId.set(deviceId);
        }

        return deviceId;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Registers the application with GCM servers asynchronously.
     *
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private static void registerInBackground(final Context context) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                try {

                    // Fetch instance of google cloud messaging object.
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

                    if (gcm != null) {

                        String registrationId = gcm.register(SENDER_ID);

                        // You should send the registration ID to your server over HTTP, so it
                        // can use GCM/HTTP or CCS to send messages to your app.
                        sendRegistrationIdToBackend(context, registrationId);

                        // Persist the id - no need to register again.
                        storeRegistrationId(context, registrationId);
                    }

                } catch (IOException ex) {

                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return null;
            }

        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private static void sendRegistrationIdToBackend(Context context, final String registrationId) {

        // Fetch device id.
        final String deviceId = getDeviceId(context);

        /*
        RestModelDevice.create(null, deviceId, new RestResult<RestModelDevice>() {

            @Override
            public void onSuccess(RestModelDevice object) {

                RestModelDevice.update(null, object.getId(), null, true, registrationId,
                        new RestResult<RestModelDevice>() {

                            @Override
                            public void onSuccess(RestModelDevice object) {

                                // Device registration persisted.
                                AppDataObject.gcmRegistrationPersisted.set(true);
                            }
                        });
            }
        });
        */
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     *
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    @SuppressWarnings("unused")
    private static String getRegistrationId(Context context) {

        // Attempt to fetch stored registration id.
        String registrationId = null;//AppDataObject.gcmRegistrationId.get();

        // Fetch the stored application version.
        int registeredVersion = 1;//AppDataObject.gcmAppVersion.get();

        // Fetch the current application version.
        int currentVersion = getAppVersion(context);

        if (registrationId == null || registeredVersion != currentVersion) {

            return null;
        }

        return registrationId;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    @SuppressWarnings("unused")
    private static boolean checkPlayServices(Activity activity) {

        // Check to see if google play services available on this device.
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {

            // If can show recovery dialog and not ignoring result.
            //if (GooglePlayServicesUtil.isUserRecoverableError(resultCode) && !AppConfig.isGcmRegistrationIgnored()) {
            //    GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
            //            PLAY_SERVICES_RESOLUTION_REQUEST).show();
            //}

            return false;
        }

        return true;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    @SuppressWarnings("unused")
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            // Should never happen.
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param registrationId registration ID
     */
    @SuppressWarnings("unused")
    private static void storeRegistrationId(Context context, String registrationId) {

        // Store registration id, with app version.
        //AppDataObject.gcmRegistrationId.set(registrationId);
        //AppDataObject.gcmAppVersion.set(getAppVersion(context));
    }

    // endregion
}