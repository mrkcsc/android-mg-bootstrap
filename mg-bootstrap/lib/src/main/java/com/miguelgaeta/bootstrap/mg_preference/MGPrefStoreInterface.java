package com.miguelgaeta.bootstrap.mg_preference;

import java.lang.reflect.Type;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
public interface MGPrefStoreInterface {

    /**
     * Get the associated object keyed to the
     * provided value.
     */
    Object get(@NonNull String key, boolean versioned);

    /**
     * Set a key into the store and persist an arbitrary
     * object.
     */
    void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned);

    /**
     * Clear the store of all data.
     */
    void clear();
}
