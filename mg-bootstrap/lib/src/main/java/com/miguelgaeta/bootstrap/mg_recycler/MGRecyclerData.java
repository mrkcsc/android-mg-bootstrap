package com.miguelgaeta.bootstrap.mg_recycler;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 4/28/15.
 */
public class MGRecyclerData<T> {

    private T data;

    @NonNull
    private DataUpdated<T> dataUpdated;

    /**
     * Private constructor.
     */
    private MGRecyclerData() {

    }

    /**
     * Static constructor.
     */
    public static <T> MGRecyclerData<T> create(T initialData, DataUpdated<T> dataUpdated) {

        MGRecyclerData<T> data = new MGRecyclerData<>();

        data.data = initialData;
        data.dataUpdated = dataUpdated;

        return data;
    }

    public T get() {

        return data;
    }

    /**
     * If data changed, set new data, store
     * old data and emit callback.
     */
    public void set(T data) {

        if (data == null ? this.data != null : !data.equals(this.data)) {

            T oldData = this.data;

            this.data = data;

            dataUpdated.updated(oldData, this.data);
        }
    }

    /**
     * Callback interface.
     */
    public interface DataUpdated<T> {

        void updated(T oldData, T newData);
    }
}
