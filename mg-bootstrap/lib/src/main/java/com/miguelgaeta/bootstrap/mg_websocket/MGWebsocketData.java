package com.miguelgaeta.bootstrap.mg_websocket;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/11/15.
 */
public class MGWebsocketData<T extends MGWebsocketData.Data> {

    @Getter
    private T data;

    public static class Data {

    }

    public static class Open extends Data {

    }

    public MGWebsocketData(T data) {

        this.data = data;
    }
}
