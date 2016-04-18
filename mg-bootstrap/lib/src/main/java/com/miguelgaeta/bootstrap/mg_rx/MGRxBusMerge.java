package com.miguelgaeta.bootstrap.mg_rx;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Miguel Gaeta on 5/4/15.
 */
@SuppressWarnings("unused")
public class MGRxBusMerge {

    /**
     * Merge a new item.  Optionally, only merge
     * if a value does not already exist.
     */
    public static <T> Func1<T, T> set(final T value, final boolean overrideExistingValue) {
        return new Func1<T, T>() {
            @Override
            public T call(T t) {
                if (overrideExistingValue || t == null) {

                    return value;
                }

                return t;
            }
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
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapMerge(final K key, final Func1<V, V> mergeFunction) {

        return new Func1<Map<K, V>, Map<K, V>>() {
            @Override
            public Map<K, V> call(Map<K, V> kvMap) {
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
            }
        };
    }

    /**
     * Merge a new item into map.  Optionally, only merge
     * if a value for this key not already present.
     */
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapPut(final K key, final V value, final boolean overrideExistingValue) {
        return new Func1<Map<K, V>, Map<K, V>>() {
            @Override
            public Map<K, V> call(Map<K, V> kvMap) {
                Map<K, V> kvMapCopy = copyMap(kvMap);

                if (overrideExistingValue || !kvMapCopy.containsKey(key)) {
                    kvMapCopy.put(key, value);
                }

                return kvMapCopy;
            }
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
    public static <K, V> Func1<Map<K, V>, Map<K, V>> mapRemove(final K key) {
        return new Func1<Map<K, V>, Map<K, V>>() {
            @Override
            public Map<K, V> call(Map<K, V> kvMap) {
                if (kvMap == null) {
                    return null;
                }

                Map<K, V> kvMapCopy = copyMap(kvMap);

                if (kvMapCopy.containsKey(key)) {
                    kvMapCopy.remove(key);
                }

                return kvMapCopy;
            }
        };
    }

    public static <K, V> Map<K, V> mergeMap(Map<K, V> map, Map<K, V> partialMap, Func0<Map<K, V>> defaultValue, Func1<V, K> keyGetter, Func2<V, V, V> valueMerger) {

        if (map == null) {
            map = defaultValue.call();
        }

        if (partialMap == null) {
            partialMap = defaultValue.call();
        }

        Map<K, V> mapCopy = MGRxBusMerge.copyMap(map);

        for (V value : partialMap.values()) {

            K key = keyGetter.call(value);

            mapCopy.put(key, valueMerger.call(mapCopy.get(key), value));
        }

        return mapCopy;
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
