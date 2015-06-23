package com.miguelgaeta.bootstrap.mg_log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        void run(Throwable throwable, String message);

        interface Info extends Callback {

        }

        interface Error extends Callback {

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

    public static StackTraceElement[] getStackTrace(StackTraceElement[] stackTrace, String exclusionTag) {

        return getStackTrace(stackTrace, Collections.singletonList(exclusionTag));
    }

    public static StackTraceElement[] getStackTrace(StackTraceElement[] stackTrace, List<String> exclusionTags) {

        ArrayList<StackTraceElement> modifiedStackTrace = new ArrayList<>();

        for (StackTraceElement element : stackTrace) {

            boolean includeElement = true;

            for (String exclusionTag : exclusionTags) {

                if (element.getClassName().contains(exclusionTag)) {

                    includeElement = false;
                }
            }

            if (includeElement) {

                modifiedStackTrace.add(element);
            }
        }

        return modifiedStackTrace.toArray(new StackTraceElement[modifiedStackTrace.size()]);
    }
}
