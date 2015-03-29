package com.miguelgaeta.bootstrap.mg_keyboard;

import lombok.Getter;

/**
 * Created by mrkcsc on 3/27/15.
 */
public class MGKeyboard {

    @Getter(lazy = true)
    private static final MGKeyboardConfig config = new MGKeyboardConfig();

    @Getter(lazy = true)
    private static final MGKeyboardMetrics metrics = new MGKeyboardMetrics();
}
