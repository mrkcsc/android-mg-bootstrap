package com.miguelgaeta.bootstrap.mg_preference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Miguel Gaeta on 4/20/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGPreferenceRxUtils {



    /**
     * Put or update a new map item in a
     * preference item.
     */
    public static <K, V> void putMapItem(MGPreferenceRx<Map<K, V>> source, K key, V value) {

        source.get().take(1).subscribe(sourceMap -> {

            nullCheck(sourceMap);

            // Update source map key.
            sourceMap.put(key, value);

            // Update the source.
            source.set(sourceMap);
        });
    }

    /**
     * Put a default item into a map of
     * preference if it doesn't exist.
     */
    public static <K, V> void putMapDefault(MGPreferenceRx<Map<K, V>> source, K key, V defaultValue) {

        source.get().take(1).subscribe(sourceMap -> {

            nullCheck(sourceMap);

            if (!sourceMap.containsKey(key)) {

                // Put default value.
                sourceMap.put(key, defaultValue);

                // Update the source.
                source.set(sourceMap);
            }
        });
    }

    /**
     * Merge a new key value pair into a preference of a
     * map of a list object.  Will merge new value into
     * the map assuming it does not already contain it.
     */
    public static <K, V> void addMapListItem(MGPreferenceRx<Map<K, List<V>>> source, K key, V value) {

        addMapListItem(source, key, value, null);
    }

    /**
     * Merge a new key value pair into a preference of a
     * map of a list object.  Will merge new value into
     * the map assuming it does not already contain it.
     */
    public static <K, V> void addMapListItem(MGPreferenceRx<Map<K, List<V>>> source, K key, V value, Integer position) {

        source.get().take(1).subscribe(sourceMap -> {

            nullCheck(sourceMap);

            // Fetch new key object.
            List<V> list = new ArrayList<>();

            // Or take from source if exists.
            if (sourceMap.containsKey(key)) {

                for (V sourceMapValue : sourceMap.get(key)) {

                    list.add(sourceMapValue);
                }
            }

            if (!list.contains(value)) {

                if (position == null) {

                    // Append.
                    list.add(value);
                } else {

                    // Insert.
                    list.add(position, value);
                }

                // Update list im map.
                sourceMap.put(key, list);

                // Update the source.
                source.set(sourceMap);
            }
        });
    }

    /**
     * Removes an item from a list value in a map
     * keyed to some arbitrary value.
     */
    public static <K, V> void removeMapListItem(MGPreferenceRx<Map<K, List<V>>> source, K key, V value) {
        
        source.get().take(1).subscribe(sourceMap -> {

            nullCheck(sourceMap);

            // Or take from source if exists.
            if (sourceMap.containsKey(key)) {

                List<V> list = new ArrayList<>();

                for (V sourceMapValue : sourceMap.get(key)) {

                    list.add(sourceMapValue);
                }

                if (list.contains(value)) {

                    // Remove value.
                    list.remove(value);

                    // Update list im map.
                    sourceMap.put(key, list);

                    // Update the source.
                    source.set(sourceMap);
                }
            }
        });
    }

    /**
     * Ensure object that is emitted by a
     * preference is not null.
     */
    private static void nullCheck(Object object) {

        if (object == null) {

            // Should not happen until source is not configured with default.
            throw new RuntimeException("Cannot merge a store stream that is null.");
        }
    }
}
