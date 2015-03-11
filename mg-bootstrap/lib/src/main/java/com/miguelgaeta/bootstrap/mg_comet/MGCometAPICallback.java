package com.miguelgaeta.bootstrap.mg_comet;

import com.google.gson.JsonObject;

/**
 * API callback for comet messages.
 */
public interface MGCometAPICallback<T> {

    // Received a message, the class is a current instance
    // of the receiving class, the message is a message received,
    // the message should contain json result and information,
    // about whether it was relieved in the background or not.
    public void messageReceived(T receivingClass, JsonObject message);
}