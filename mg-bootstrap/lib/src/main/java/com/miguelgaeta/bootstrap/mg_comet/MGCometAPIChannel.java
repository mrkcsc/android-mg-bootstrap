package com.miguelgaeta.bootstrap.mg_comet;

import android.app.Activity;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miguel on 7/21/2014. Copyright 2014 Blitz Studios
 */
public class MGCometAPIChannel {

    // region Member Variables
    // ============================================================================================================

    // Channel name.
    private String mName;

    // Channel cursor.
    private int mCursor;

    // Channel callbacks and queued messages.
    // TODO: Use a single map, use custom class, not a pair.
    private HashMap<String, Pair<MGCometAPICallback, Class>> mCallbacks;
    private HashMap<String, ArrayList<JsonObject>> mCallbacksQueuedMessages;

    // endregion

    // region Constructors
    // ============================================================================================================

    /**
     * Private empty constructor.
     */
    @SuppressWarnings("unused")
    private MGCometAPIChannel() {

        // Initialize hash map of callbacks.
        mCallbacks = new HashMap<>();

        // Initialize array of queued messages.
        mCallbacksQueuedMessages = new HashMap<>();
    }

    /**
     * Initialize a channel.
     *
     * @param name Channel name.
     * @param cursor Channel cursor.
     */
    MGCometAPIChannel(String name, int cursor) {
        this();

        // Set channel name.
        mName = name;

        // Set channel cursor.
        mCursor = cursor;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Add a callback to the channel that is executed when
     * messages are received from the websocket.
     *
     * @param receivingClassObject The class object that is going to be receiving this
     *                             callback. Must be either a fragment or activity.
     *
     * @param callback The callback to be executed.  Guaranteed to execute
     *                 with a current and active instance of the receiving class
     *                 as a callback parameter.  Use this class to perform
     *                 operations if needed to avoid creating memory leaks.
     *
     * @param callbackIdentifier String to identify this callback, must be
     *                           non-null.  Used for callback removal and
     *                           to prevent duplicate callbacks.
     *
     * @param <T> Type of the receiving class.
     */
    @SuppressWarnings({"unused", "unchecked"})
    public <T> void addCallback(T receivingClassObject, MGCometAPICallback<T> callback, String callbackIdentifier) {

        // Fetch class from the class object provided.
        Class receivingClass = receivingClassObject.getClass();

        // Add callback using class parameter.
        addCallback(receivingClass, callback, callbackIdentifier);
    }

    /**
     * @param receivingClass Class that is going to be receiving
     *                       this callback. Must be either a fragment
     *                       or activity.
     */
    @SuppressWarnings("unused")
    public <T> void addCallback(Class<T> receivingClass, MGCometAPICallback<T> callback, String callbackIdentifier) {

        /*
        // Verify receiving class is supported.
        if (!BaseActivity.class.isAssignableFrom(receivingClass) &&
            !BaseFragment.class.isAssignableFrom(receivingClass)) {

            // Throw exception.
            throw new RuntimeException("Receiving class must be a fragment or activity.");
        }
        */

        // If identifier provided.
        if (callbackIdentifier != null) {

            // Add callback, with receiving class.
            mCallbacks.put(callbackIdentifier,
                    new Pair<MGCometAPICallback, Class>(callback, receivingClass));
        }
    }

    /**
     * Remove callback with specified identifier.
     *
     * @param callbackIdentifier Callback identifier.
     */
    @SuppressWarnings("unused")
    public void removeCallback(String callbackIdentifier) {

        // Remove requested callbacks.
        mCallbacks.remove(callbackIdentifier);

        // Also remove queued messages.
        mCallbacksQueuedMessages.remove(callbackIdentifier);
    }

    /**
     * Remove all associated callbacks.
     */
    @SuppressWarnings("unused")
    public void removeCallbacks() {

        // Clear callbacks.
        mCallbacks.clear();

        // And queued messages.
        mCallbacksQueuedMessages.clear();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Add a message to the queue.  Each message is
     * associated with a particular callback.
     *
     * @param callbackIdentifier Callback identifier.
     *
     * @param message Message to queue.
     */
    private void addQueuedMessage(String callbackIdentifier, JsonObject message) {

        // Fetch queued messages for this callback identifier.
        ArrayList<JsonObject> queuedMessages = mCallbacksQueuedMessages.get(callbackIdentifier);

        // Initialize if needed.
        if (queuedMessages == null) {
            queuedMessages = new ArrayList<JsonObject>();
        }

        // Add to queue.
        queuedMessages.add(message);

        // Insert into dictionary.
        mCallbacksQueuedMessages.put(callbackIdentifier, queuedMessages);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void sendMessage(final MGCometAPICallback callback, final Object activityOrFragment, final JsonObject jsonObject) {

        Activity associatedActivity = null;

        /*
        // If object is activity.
        if (BaseActivity.class.isAssignableFrom(activityOrFragment.getClass())) {

            associatedActivity = (BaseActivity)activityOrFragment;
        }

        // If object is fragment.
        if (BaseFragment.class.isAssignableFrom(activityOrFragment.getClass())) {

            associatedActivity = ((BaseFragment)activityOrFragment).getActivity();
        }
        */

        // Since the message will always be sent to
        // an activity or fragment, make it easy on
        // the callee and run result on UI thread.
        associatedActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Fetch associated data payload.
                JsonObject data = jsonObject.getAsJsonObject("data");

                if (data != null) {

                    // Send the callbackEntry with current activity/fragment as the receiving class.
                    callback.messageReceived(activityOrFragment, jsonObject.getAsJsonObject("data"));
                }
            }
        });
    }

    // endregion

    // region Package Methods
    // ============================================================================================================

    /**
     * Get a json string of this channel to
     * send to the websocket.
     *
     * @param subscribe Subscribe or unsubscribe.
     *
     * @return Json string.
     */
    @SuppressWarnings("unused")
    String getJsonString(boolean subscribe) {

        // Initialize dictionary.
        HashMap<String, Object> jsonDictionary = new HashMap<String, Object>();

        // Place parameters into dictionary.
        jsonDictionary.put("action", subscribe ? "subscribe" : "unsubscribe");
        jsonDictionary.put("channel", mName);
        jsonDictionary.put("cursor", mCursor);

        // Convert to json string.
        return new Gson().toJsonTree(jsonDictionary).toString();
    }

    /**
     * Try to send off queued messages, when
     * an activity or fragment becomes active.
     *
     * @param activityOrFragment Activity or fragment.
     */
    @SuppressWarnings("unchecked")
    void trySendQueuedMessages(Object activityOrFragment) {

        // Iterate over each callback associated with the channel.
        for (Map.Entry<String, Pair<MGCometAPICallback, Class>> callbackEntry : mCallbacks.entrySet()) {

            // Fetch callback activityOrFragment pair (receiving class, to callback).
            Pair<MGCometAPICallback, Class> callback = callbackEntry.getValue();

            // If callbackEntry receiving class matched.
            if (callback.second.equals(activityOrFragment.getClass())) {

                // Attempt to fetch queued json objects.
                ArrayList<JsonObject> jsonObjects = mCallbacksQueuedMessages.get(callbackEntry.getKey());

                if (jsonObjects != null) {

                    // Iterate over queued messages.
                    for (JsonObject jsonObject : jsonObjects) {

                        // Send the message.
                        sendMessage(callback.first, activityOrFragment, jsonObject);
                    }

                    // Remove message for key from queue.
                    mCallbacksQueuedMessages.remove(callbackEntry.getKey());
                }
            }
        }
    }

    /**
     * Try to send message to callbacks that are
     * listening on a current active activity or fragment.
     *
     * @param jsonObject Json message.
     * @param activityOrFragments Activity or fragment.
     */
    void trySendMessages(JsonObject jsonObject, ArrayList<Object> activityOrFragments) {

        for (Map.Entry<String, Pair<MGCometAPICallback, Class>> callbackEntry : mCallbacks.entrySet()) {

            // Fetch callback object pair (receiving class, to callback).
            Pair<MGCometAPICallback, Class> callback = callbackEntry.getValue();

            // If we cannot find a current activity
            // or fragment that matches the callbacks
            // receiving class, we need to store it
            // to be sent later.
            boolean callbackShouldBeQueued = true;

            // Iterate over active activity and fragments.
            for (Object currentActivityOrFragment : activityOrFragments) {

                // If callbackEntry receiving class matched.
                if (callback.second.equals(currentActivityOrFragment.getClass())) {

                    // Send the message.
                    sendMessage(callback.first, currentActivityOrFragment, jsonObject);

                    // Callback ran, no need to queue.
                    callbackShouldBeQueued = false;
                }
            }

            if (callbackShouldBeQueued) {

                // Add a queued message for this particular callback identifier.
                addQueuedMessage(callbackEntry.getKey(), jsonObject);
            }
        }
    }

    // endregion
}