package com.miguelgaeta.bootstrap.mg_rx;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
     * Merge new item into map.
     */
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapMerge(K key, Func1<V, V> mergeFunction) {

        return kvMap -> {

            V mergedValue = mergeFunction.call(kvMap.get(key));

            if (!kvMap.containsKey(key) || kvMap.get(key) == null || !kvMap.get(key).equals(mergedValue)) {

                Map<K, V> kvMapCopy = copyMap(kvMap);

                if (mergedValue == null) {

                    kvMapCopy.remove(key);

                } else {

                    kvMapCopy.put(key, mergedValue);
                }

                return kvMapCopy;
            }

            return kvMap;
        };
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

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> copyMap(Map<K, V> source) {

        Map<K, V> sourceItemCopy;

        if (source instanceof HashMap) {

            return (Map<K, V>) ((HashMap<K, V>) source).clone();

        } else if (source instanceof TreeMap) {

            return (Map<K, V>) ((TreeMap<K, V>) source).clone();

        } else {

            throw new RuntimeException("Unsupported map, cannot copy: " + source.getClass());
        }
    }
}
