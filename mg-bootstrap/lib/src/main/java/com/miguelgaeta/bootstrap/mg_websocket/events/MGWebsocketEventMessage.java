package com.miguelgaeta.bootstrap.mg_websocket.events;

import java.nio.ByteBuffer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mrkcsc on 4/15/15.
 */
@AllArgsConstructor(staticName = "create") @Getter @ToString @EqualsAndHashCode
public class MGWebsocketEventMessage {

    private final String string;

    private final ByteBuffer bytes;

    @Getter(lazy = true)
    private transient final boolean binary = string == null && bytes != null;
}
