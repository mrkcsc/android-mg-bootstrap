package com.miguelgaeta.bootstrap.mg_dates;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by mrkcsc on 5/3/15.
 */
public class MGDates {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final PrettyTime prettyTime = new PrettyTime();

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull DateTime dateTime, boolean hasPostfix) {

        return format(dateTime.toDate(), hasPostfix);
    }

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull DateTime dateTime) {

        return format(dateTime, true);
    }

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull Date date, boolean hasPostfix) {

        String formattedDate = getPrettyTime().format(date).replace("moments ago", "just now");

        if (!hasPostfix) {

            formattedDate = formattedDate.replace("ago", "");
        }

        return formattedDate;
    }

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull Date date) {

        return format(date, true);
    }
}
