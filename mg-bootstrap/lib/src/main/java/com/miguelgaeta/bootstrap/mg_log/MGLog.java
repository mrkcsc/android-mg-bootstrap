package com.miguelgaeta.bootstrap.mg_log;

import lombok.Getter;
import timber.log.Timber;

/**
 * Created by mrkcsc on 3/15/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGLog {

    @Getter
    private static MGLogConfig config = new MGLogConfig();

    /**
     * Callback info and error interfaces.
     */
    public interface Callback {

        interface Info {

            void run(Throwable throwable, String message);
        }

        interface Error {

            void run(Throwable throwable, String message);
        }
    }

    /**
     * Info level message.
     */
    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    /**
     * Info level message with throwable.
     */
    public static void i(Throwable t, String message, Object... args) {
        Timber.i(t, message, args);
    }

    /**
     * Error level message.
     */
    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }

    /**
     * Error level message with throwable.
     */
    public static void e(Throwable t, String message, Object... args) {
        Timber.e(t, message, args);
    }
}
