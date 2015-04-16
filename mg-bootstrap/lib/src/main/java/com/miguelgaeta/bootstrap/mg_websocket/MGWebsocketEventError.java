package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mrkcsc on 4/15/15.
 */
@AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
public class MGWebsocketEventError {

    // Exception.
    private Exception exception;
}
