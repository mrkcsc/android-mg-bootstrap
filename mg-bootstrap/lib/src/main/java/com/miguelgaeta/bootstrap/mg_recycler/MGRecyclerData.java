package com.miguelgaeta.bootstrap.mg_recycler;

import lombok.NonNull;
import lombok.Setter;

/**
 * Created by Miguel Gaeta on 4/28/15.
 */
public class MGRecyclerData<T> {

    private T data;

    @NonNull @Setter
    private DataUpdated<T> updater;

    private MGRecyclerData() { }

    /**
     * Static constructor.
     */
    public static <T> MGRecyclerData<T> create(T initialData, DataUpdated<T> updater) {

        MGRecyclerData<T> data = new MGRecyclerData<>();

        data.data = initialData;
        data.updater = updater;

        return data;
    }

    public T get() {

        return data;
    }

    public void set(T data) {

        this.data = data;
    }

    /**
     * Triggers an update event for a new data set.  Typically
     * can be used by an adapter or utility to diff the
     * changes and if needed call a formal set on the
     * new data.
     */
    public void update(T data) {

        updater.updated(this.data, data);
    }

    /**
     * Callback interface.
     */
    public interface DataUpdated<T> {

        void updated(T oldData, T newData);
    }
}
