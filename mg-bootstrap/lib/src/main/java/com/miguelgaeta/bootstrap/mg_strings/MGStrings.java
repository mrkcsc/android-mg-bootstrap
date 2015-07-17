package com.miguelgaeta.bootstrap.mg_strings;

import java.util.List;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGStrings {

    @Getter(lazy = true)
    private static final Pattern camelCaseSplitter = Pattern.compile(String.format("%s|%s|%s",

        "(?<=[A-Z])(?=[A-Z][a-z])",

        "(?<=[^A-Z])(?=[A-Z])",

        "(?<=[A-Za-z])(?=[^A-Za-z])"));

    /**
     * Strip away any matching strings from the target string
     * and prefix them to the start of the string.
     */
    public static String stripAndPrefix(@NonNull String targetString, @NonNull List<String> strings) {

        for (String string : strings) {

            if (targetString.contains(string)) {
                targetString = targetString.replace(string, "");

                targetString = string + targetString;
            }
        }

        return targetString;
    }

    /**
     * Take a string that is camel cased and convert it into
     * a lowercase string where each upper cased character is
     * separated with an underscore.
     */
    public static String camelCaseToLowerCaseUnderscores(@NonNull String targetString) {

        return getCamelCaseSplitter().matcher(targetString).replaceAll("_").toLowerCase();
    }

    /**
     * Capitalize the first letter of a string.  Accepts
     * null strings and one letter strings.
     */
    public static String capitalizeFirstLetter(String targetString) {

        if (targetString != null) {

            Character firstLetter = Character.toUpperCase(targetString.charAt(0));

            if (targetString.length() > 1) {

                return firstLetter + targetString.substring(1);
            }

            return Character.toString(firstLetter);
        }

        return null;
    }
}
