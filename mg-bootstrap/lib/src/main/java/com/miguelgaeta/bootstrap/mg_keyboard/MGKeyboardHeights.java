package com.miguelgaeta.bootstrap.mg_keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrkcsc on 3/29/15.
 */
class MGKeyboardHeights {

    // The actual data structure holding the keyboard heights.
    private final Map<String, List<Integer>> keyboardHeights = new HashMap<>();

    /**
     * Given a soft keyboard identifier, fetch
     * recognized associated keyboard heights.
     */
    List<Integer> get(String softKeyboardIdentifier) {

        return keyboardHeights.get(softKeyboardIdentifier) != null ?
                keyboardHeights.get(softKeyboardIdentifier) : new ArrayList<>();
    }

    /**
     * Given a soft keyboard identifier, is a given
     * height present in the associated keyboard heights.
     */
    boolean contains(String softKeyboardIdentifier, int keyboardHeight) {

        return keyboardHeights.get(softKeyboardIdentifier) != null &&
                keyboardHeights.get(softKeyboardIdentifier).contains(keyboardHeight);
    }

    /**
     * Add a new keyboard height to an associated
     * soft keyboard identifier.
     */
    void add(String softKeyboardIdentifier, int keyboardHeight) {

        List<Integer> heights = keyboardHeights.get(softKeyboardIdentifier);

        if (heights == null) {
            heights = new ArrayList<>();
        }

        if (!heights.contains(keyboardHeight)) {
            heights.add(keyboardHeight);
        }

        keyboardHeights.put(softKeyboardIdentifier, heights);
    }
}
