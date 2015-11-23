package com.miguelgaeta.bootstrap.keyboarder;

import android.content.Context;
import android.provider.Settings;

import com.miguelgaeta.bootstrap.mg_preference.MGPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 11/23/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class KeyboarderHeight {

    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private static final MGPreference<Map<String, List<Integer>>> keyboardHeights = MGPreference.create("KEYBOARDER_HEIGHTS");

    static void addKeyboardHeight(@NonNull Context context, int height) {

        final String identifier = getSoftKeyboardIdentifier(context);

        final Map<String, List<Integer>> keyboardHeights = getKeyboardHeights().get();

        if (!keyboardHeights.containsKey(identifier)) {
             keyboardHeights.put(identifier, new ArrayList<>());
        }

        if (!keyboardHeights.get(identifier).contains(height)) {
             keyboardHeights.get(identifier).add(height);

            getKeyboardHeights().set(keyboardHeights);
        }
    }

    private static String getSoftKeyboardIdentifier(@NonNull Context context) {

        // Fetch soft keyboard identifier using a cool trick: http://bit.ly/18w0yTJ
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
    }
}
