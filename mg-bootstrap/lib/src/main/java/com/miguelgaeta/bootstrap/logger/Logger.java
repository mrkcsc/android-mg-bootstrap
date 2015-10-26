package com.miguelgaeta.bootstrap.logger;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Miguel Gaeta on 10/26/15.
 */
public class Logger {

    @RequiredArgsConstructor(staticName = "create")
    public static class Elapsed {

        public final long startTime = DateTime.now().getMillis();

        @Getter(lazy = true)
        private final long milliseconds = DateTime.now().getMillis() - startTime;

        @Getter(lazy = true)
        private final float seconds = getMilliseconds() / 1000.0f;
    }
}
