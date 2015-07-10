package com.miguelgaeta.bootstrap.mg_recycler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.NonNull;
import lombok.Setter;
import rx.functions.Func1;

/**
 * Created by Miguel Gaeta on 5/2/15.
 *
 * TODO: int lastFirstVisiblePosition = ((LinearLayoutManager)getRecycler().getLayoutManager()).findFirstCompletelyVisibleItemPosition();
 */
public class MGRecyclerDataList<T> {

    private MGRecyclerData<List<T>> data;

    @Setter
    private MGRecyclerData.DataUpdated<List<T>> updated;

    /**
     * Convenience class that represents an arbitrary object
     * list that drives an adapter.
     *
     * When new data is provided to this object, the associated
     * adapter (which should be drive by the list) will
     * be updated by this helper class which will compute
     * the minimal notify changed needed.
     *
     * A key generator is provided so that a unique
     * key can be generated for each object in the list.  There
     * should not be any duplicate keys in a given data list.
     */
    public static <T> MGRecyclerDataList<T> create(@NonNull MGRecyclerAdapter adapter, List<T> initialData, Func1<T, String> keyGenerator) {

        MGRecyclerDataList<T> dataList = new MGRecyclerDataList<>();

        dataList.data = MGRecyclerData.create(initialData, (oldData, newData) -> {

            LinkedHashMap<String, Integer> oldDataIndexes = dataList.generateIndexMap(oldData, keyGenerator);
            LinkedHashMap<String, Integer> newDataIndexes = dataList.generateIndexMap(newData, keyGenerator);

            dataList.changeItems(adapter, oldDataIndexes, newDataIndexes, oldData, newData);

            dataList.removeItems(adapter, oldData, newDataIndexes, keyGenerator);
            dataList.insertItems(adapter, newData, oldDataIndexes, keyGenerator);

            if (dataList.updated != null) {
                dataList.updated.updated(oldData, newData);
            }
        });

        return dataList;
    }

    /**
     * Set a new data list.  Computes all the
     * changes and calls the associated
     * adapter functions.
     */
    public void set(List<T> data) {

        this.data.set(data);
    }

    /**
     * Grab the current data list.
     */
    public List<T> get() {

        return data.get();
    }

    /**
     * Figure out which items were changed in the list.  Potentially
     * someday this function can do a more comprehensive
     * analysis and do actual item moves instead of just
     * notifying that the item at index has changed.
     */
    private void changeItems(MGRecyclerAdapter adapter, LinkedHashMap<String, Integer> oldDataIndexes, LinkedHashMap<String, Integer> newDataIndexes, List<T> oldData, List<T> newData) {

        List<String> keys = new ArrayList<>(oldDataIndexes.keySet());

        for (int i = 0; i < oldDataIndexes.size(); i++) {

            String key = keys.get(i);

            if (newDataIndexes.containsKey(key)) {

                int oldIndex = oldDataIndexes.get(key);
                int newIndex = newDataIndexes.get(key);

                T oldDataItem = oldData.get(oldIndex);
                T newDataItem = newData.get(newIndex);

                if (oldDataItem == null ? newDataItem != null : !oldDataItem.equals(newDataItem)) {

                    adapter.notifyItemChanged(oldIndex);
                }
            }
        }
    }

    /**
     * Figure out which items were inserted
     * into the list.
     */
    private void insertItems(MGRecyclerAdapter adapter, List<T> newData, LinkedHashMap<String, Integer> oldDataIndexes, Func1<T, String> keyGenerator) {

        int count = 0;

        int startIndex = 0;

        for (int i = 0; i < newData.size(); i++) {

            if (!oldDataIndexes.containsKey(keyGenerator.call(newData.get(i)))) {

                if (count == 0) {
                    startIndex = i;
                }

                count++;

            } else if (count > 0) {

                adapter.notifyItemRangeInserted(startIndex, count);

                count = 0;
            }
        }

        if (count > 0) {

            adapter.notifyItemRangeInserted(startIndex, count);
        }
    }

    /**
     * Figure out which items were removed
     * from the list of data.
     */
    private void removeItems(MGRecyclerAdapter adapter, List<T> oldData, LinkedHashMap<String, Integer> newDataIndexes, Func1<T, String> keyGenerator) {

        int count = 0;

        int startIndex = 0;
        int startIndexOffset = 0;

        for (int i = 0; i < oldData.size(); i++) {

            if (!newDataIndexes.containsKey(keyGenerator.call(oldData.get(i)))) {

                if (count == 0) {
                    startIndex = i + startIndexOffset;
                }

                count++;

            } else if (count > 0) {

                adapter.notifyItemRangeRemoved(startIndex, count);

                startIndexOffset -= count;

                count = 0;
            }
        }

        if (count > 0) {

            adapter.notifyItemRangeRemoved(startIndex, count);
        }
    }

    /**
     * Given a list of data and a key generator function.  Create
     * a linked hash map of keys to associated index positions.
     */
    private LinkedHashMap<String, Integer> generateIndexMap(@NonNull List<T> data, Func1<T, String> keyGenerator) {

        LinkedHashMap<String, Integer> indexMap = new LinkedHashMap<>();

        for (int index = 0; index < data.size(); index++) {

            indexMap.put(keyGenerator.call(data.get(index)), index);
        }

        return indexMap;
    }
}
