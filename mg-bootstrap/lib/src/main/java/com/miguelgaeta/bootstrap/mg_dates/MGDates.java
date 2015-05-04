package com.miguelgaeta.bootstrap.mg_dates;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import lombok.NonNull;

/**
 * Created by mrkcsc on 5/3/15.
 */
public class MGDates {

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull Context context, @NonNull DateTime dateTime) {

        return format(context, dateTime, true);
    }

    /**
     * Format date tim using the "ago"
     * human readable type style.
     */
    public static String format(@NonNull Context context, @NonNull DateTime dateTime,  boolean hasPostfix) {

        String formattedDate = DateUtils.getRelativeTimeSpanString(context, dateTime).toString();

        if (formattedDate.equals("0 seconds ago")) {

            return "just now";
        }

        if (!hasPostfix) {

            formattedDate = formattedDate.replace("ago", "");
        }

        return formattedDate;
    }
}
