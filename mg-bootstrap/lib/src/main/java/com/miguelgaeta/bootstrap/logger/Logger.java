package com.miguelgaeta.bootstrap.logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Miguel Gaeta on 10/26/15.
 */
public class Logger {

    @RequiredArgsConstructor(staticName = "create")
    public static class Elapsed {

        public final long startTime = System.currentTimeMillis();

        @Getter(lazy = true)
        private final long milliseconds = System.currentTimeMillis() - startTime;

        @Getter(lazy = true)
        private final float seconds = getMilliseconds() / 1000.0f;
    }
}
