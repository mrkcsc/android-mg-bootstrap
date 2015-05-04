package com.miguelgaeta.bootstrap.mg_strings;

import java.util.List;

import lombok.NonNull;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGStrings {

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

        StringBuilder sb = new StringBuilder();

        for (Character character : targetString.toCharArray()) {

            if ((Character.isUpperCase(character) || Character.isDigit(character))) {

                if (sb.length() > 0) {
                    sb.append("_");
                }

                sb.append(Character.toLowerCase(character));
            } else {
                sb.append(character);
            }
        }

        return sb.toString();
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
