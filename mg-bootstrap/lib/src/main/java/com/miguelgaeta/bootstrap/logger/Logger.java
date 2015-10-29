package com.miguelgaeta.bootstrap.logger;

import com.miguelgaeta.bootstrap.dates.SimpleTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Miguel Gaeta on 10/26/15.
 */
public class Logger {

    @RequiredArgsConstructor(staticName = "create")
    public static class Elapsed {

        public final long startTime = SimpleTime.getNow();

        @Getter(lazy = true)
        private final long milliseconds = SimpleTime.getNow() - startTime;

        @Getter(lazy = true)
        private final float seconds = getMilliseconds() / 1000.0f;
    }
}
