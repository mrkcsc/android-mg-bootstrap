package com.miguelgaeta.bootstrap.mg_websocket.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mrkcsc on 4/15/15.
 */
@AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
public class MGWebsocketEventClosed {

    // Status code.
    private int code;

    // Closed reason.
    private String reason;

    // Remotely closed.
    private boolean remote;
}
