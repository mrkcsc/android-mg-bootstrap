package com.miguelgaeta.bootstrap.mg_gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class MGCMIntentService extends IntentService {

    // region Member Variables
    // =============================================================================================================

    public static final int NOTIFICATION_ID = 1;

    // endregion

    // region Constructors
    // =============================================================================================================

    /**
     * Default constructor,  The string passed to the
     * super method is used for debugging purposes only.
     */
    public MGCMIntentService() {

        super(MGCMIntentService.class.toString());
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================================

    /**
     * Todo: Revise and make this method more robust.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        // Fetch instance of GCM services.
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // Fetch GCM message type parameter.
        String messageType = gcm.getMessageType(intent);

        // Fetch extras.
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {

            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

                MGLog.e("GCM error, please revise.");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {


                // Post notification of received message.
                sendNotification(extras.getString("message", null));
            }
        }

        // Release wake lock provided by WakefulBroadcastReceiver.
        MGCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // endregion

    // region Private Methods
    // =============================================================================================================

    /**
     * TODO: Supply the intent as a parameter (make this part of a reusable library).
     *
     * @param msg Target message from server.
     */
    private void sendNotification(String msg) {

        //if (msg == null) {
        //    return;
        //}

        //String title = getResources().getString(R.string.app_name);

        /*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_blitz)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(msg);

        builder.setContentIntent( // supply an intent to send when the notification is clicked
                PendingIntent.getActivity( // retrieve an intent that will start a new activity
                        getApplicationContext(), 0,
                        new Intent(this, LoadingScreen.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));
                        */

        // Fetch instance of the notification manager.
        //NotificationManager notificationManager = (NotificationManager)
        //        getSystemService(Context.NOTIFICATION_SERVICE);

        // Push out our notification.
        //notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // endregion
}