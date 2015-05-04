package com.miguelgaeta.bootstrap.mg_rx;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
@SuppressWarnings("unused")
public class MGRxBusMerge {

    /**
     * Merge a new item.  Optionally, only merge
     * if a value does not already exist.
     */
    public static <T> Func1<T, T> set(T value, boolean overrideExistingValue) {

        return t -> {

            if (overrideExistingValue || t == null) {

                return value;
            }

            return t;
        };
    }

    /**
     * Merge a new item.
     */
    public static <T> Func1<T, T> set(T value) {

        return set(value, true);
    }

    /**
     * Merge a new item into map.  Optionally, only merge
     * if a value for this key not already present.
     */
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapPut(K key, V value, boolean overrideExistingValue) {

        return kvMap -> {

            Map<K, V> kvMapCopy = copyMap(kvMap);

            if (overrideExistingValue || !kvMapCopy.containsKey(key)) {

                kvMapCopy.put(key, value);
            }

            return kvMapCopy;
        };
    }

    /**
     * Merge a new item into map.
     */
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapPut(K key, V value) {

        return mapPut(key, value, true);
    }

    /**
     * Merge function for removing a map item.
     */
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapRemove(K key) {

        return kvMap -> {

            Map<K, V> kvMapCopy = copyMap(kvMap);

            if (kvMapCopy.containsKey(key)) {
                kvMapCopy.remove(key);
            }

            return kvMapCopy;
        };
    }

    private static <K, V> Map<K, V> copyMap(Map<K, V> source) {

        Map<K, V> sourceItemCopy;

        if (source instanceof HashMap) {

            sourceItemCopy = new HashMap<>();

        } else {

            throw new RuntimeException("Unsupported map, cannot copy.");
        }

        for (Map.Entry<K, V> entry : source.entrySet()) {

            sourceItemCopy.put(entry.getKey(), entry.getValue());
        }

        return sourceItemCopy;
    }
}
