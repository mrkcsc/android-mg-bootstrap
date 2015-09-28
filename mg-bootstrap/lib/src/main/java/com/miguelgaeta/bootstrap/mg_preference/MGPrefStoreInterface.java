package com.miguelgaeta.bootstrap.mg_preference;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
interface MGPrefStoreInterface {

    /**
     * Get the associated object keyed to the
     * provided value.
     */
    Object get(@NonNull String key);

    /**
     * Set a key into the store and persist an arbitrary
     * object.
     */
    void set(@NonNull String key, Object value);

    /**
     * Clear the store of all data.
     */
    void clear();
}
