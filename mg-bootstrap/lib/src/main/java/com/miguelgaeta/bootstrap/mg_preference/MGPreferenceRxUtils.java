package com.miguelgaeta.bootstrap.mg_preference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 4/20/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGPreferenceRxUtils {

    //////////////////////////////
    /// GENERAL
    //////////////////////////////

    /**
     * Set an MG Preference rx item with generic
     * error check handling, and equality checks.
     */
    public static <T> void setItem(MGPreferenceRx<T> source, T item, boolean checkEquality) {

        takeLatest(source, sourceItem -> {

            if (!checkEquality || !sourceItem.equals(item)) {

                source.set(item);
            }
        });
    }

    public static <T> void setItem(MGPreferenceRx<T> source, T item) {

        setItem(source, item, true);
    }

    //////////////////////////////
    /// MAPS
    //////////////////////////////

    /**
     * Merge a map item into a preference item.
     */
    public static <K, V> void mergeMapItem(MGPreferenceRx<Map<K, V>> source, K key, Func1<V, V> existingItem) {

        takeLatest(source, sourceItem -> {

            V mergedItem = existingItem.call(sourceItem.containsKey(key) ? sourceItem.get(key) : null);

            // If result is merge is a new value, put new value and update stream.
            if (!sourceItem.containsKey(key) || !sourceItem.get(key).equals(mergedItem)) {

                sourceItem.put(key, mergedItem);
                source.set(sourceItem);
            }
        });
    }

    /**
     * Put or update a new map item in a
     * preference item.
     */
    public static <K, V> void putMapItem(MGPreferenceRx<Map<K, V>> source, K key, V value) {

        takeLatest(source, sourceMap -> {

            // Update source map key.
            sourceMap.put(key, value);
            source.set(sourceMap);
        });
    }

    /**
     * Put a default item into a map of
     * preference if it doesn't exist.
     */
    public static <K, V> void putMapDefault(MGPreferenceRx<Map<K, V>> source, K key, V defaultValue) {

        takeLatest(source, sourceMap -> {

            if (!sourceMap.containsKey(key)) {

                // Put default value.
                sourceMap.put(key, defaultValue);
                source.set(sourceMap);
            }
        });
    }

    //////////////////////////////
    /// MAPS LISTS
    //////////////////////////////

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

        takeLatest(source, sourceMap -> {

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

        takeLatest(source, sourceMap -> {

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

    //////////////////////////////
    /// PRIVATE
    //////////////////////////////

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

    /**
     * Take latest value of preference with a
     * null check.
     */
    private static <T> void takeLatest(MGPreferenceRx<T> source, Action1<? super T> callback) {

        source.get().take(1).subscribe(sourceResult -> {

            // Make sure source not null.
            nullCheck(sourceResult);

            // Invoke callback.
            callback.call(sourceResult);
        });
    }
}
