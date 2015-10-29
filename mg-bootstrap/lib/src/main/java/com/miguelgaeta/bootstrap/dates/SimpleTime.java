package com.miguelgaeta.bootstrap.dates;

import android.content.Context;
import android.support.annotation.Nullable;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 10/28/15.
 */
@SuppressWarnings("unused")
public class SimpleTime {

    public static long getNow() {

        return DateTime.now().getMillis();
    }

    public static String getNowString() {

        return DateTime.now().toString();
    }

    public static String toDateString(@Nullable Long dateMillis) {

        return dateMillis != null ? new DateTime(dateMillis).toString() : null;
    }

    public static String toRelativeTimeSpan(@NonNull Context context, @NonNull Long dateMillis) {

        return toRelativeTimeSpan(context, dateMillis, true);
    }

    public static String toRelativeTimeSpan(@NonNull Context context, @NonNull Long dateMillis,  boolean hasPostfix) { // TODO: Just compare manually.

        String formattedDate = DateUtils.getRelativeTimeSpanString(context, new DateTime(dateMillis)).toString();

        if (formattedDate.equals("0 seconds ago") || formattedDate.equals("1 seconds ago")) {

            return "just now";
        }

        if (!hasPostfix) {

            formattedDate = formattedDate.replace("ago", "");
        }

        return formattedDate;
    }

    public static long fromSnowflake(@Nullable Long snowflake) {

        if (snowflake == null) {
            snowflake = 0L;
        }

        return (snowflake >> 22) + 1420070400000L;
    }

    public static long fromDateString(@Nullable String dateTime) {

        return fromDate(dateTime != null ? new DateTime(dateTime) : new DateTime(0L));
    }

    public static long fromDate(@Nullable DateTime date) {

        return date != null ? date.getMillis() : 0L;
    }
}
