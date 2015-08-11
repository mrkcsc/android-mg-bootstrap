package com.miguelgaeta.bootstrap.mg_recycler;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Miguel Gaeta on 5/2/15.
 */
public class MGRecyclerDataList<T> {

    private MGRecyclerData<List<T>> data;

    private Subscription updateSubscription;

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

            if (newData == null ? oldData != null : !newData.equals(oldData)) {

                // On changes, perform diffing on a computation thread.
                Observable<AdapterUpdateData> worker = Observable.create(subscriber -> {

                    final LinkedHashMap<String, Integer> oldDataIndexes = dataList.generateIndexMap(oldData, keyGenerator);
                    final LinkedHashMap<String, Integer> newDataIndexes = dataList.generateIndexMap(newData, keyGenerator);

                    final List<Integer> indicesChanged = dataList.changeItems(oldDataIndexes, newDataIndexes, oldData, newData);

                    final List<AdapterUpdateData.DataRange> indicesRangeRemoved = dataList.removeItems(oldData, newDataIndexes, keyGenerator);
                    final List<AdapterUpdateData.DataRange> indicesRangeInserted = dataList.insertItems(newData, oldDataIndexes, keyGenerator);

                    subscriber.onNext(AdapterUpdateData.create(indicesChanged, indicesRangeRemoved, indicesRangeInserted));
                    subscriber.onCompleted();
                });

                if (dataList.updateSubscription != null) {
                    dataList.updateSubscription.unsubscribe();
                }

                // Run callbacks and adapter update events on the main thread.
                dataList.updateSubscription = worker.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(adapterUpdateData -> {

                    // Set new data.
                    dataList.data.set(newData);

                    // Trigger adapter updates.
                    adapterUpdateData.update(adapter);

                    if (dataList.updated != null) {
                        dataList.updated.updated(oldData, newData);
                    }
                });
            }
        });

        return dataList;
    }

    /**
     * Triggers a lazy update of the backed
     * data object.  After performing
     * a diff it may or may not commit the new data.
     */
    public void set(List<T> data) {

        this.data.update(data);
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
    private List<Integer> changeItems(LinkedHashMap<String, Integer> oldDataIndexes, LinkedHashMap<String, Integer> newDataIndexes, List<T> oldData, List<T> newData) {

        final List<String> keys = new ArrayList<>(oldDataIndexes.keySet());

        final List<Integer> changed = new ArrayList<>();

        for (int i = 0; i < oldDataIndexes.size(); i++) {

            String key = keys.get(i);

            if (newDataIndexes.containsKey(key)) {

                int oldIndex = oldDataIndexes.get(key);
                int newIndex = newDataIndexes.get(key);

                T oldDataItem = oldData.get(oldIndex);
                T newDataItem = newData.get(newIndex);

                if (oldDataItem == null ? newDataItem != null : !oldDataItem.equals(newDataItem)) {

                    changed.add(oldIndex);
                }
            }
        }

        return changed;
    }

    /**
     * Figure out which items were inserted
     * into the list.
     */
    private List<AdapterUpdateData.DataRange> insertItems(List<T> newData, LinkedHashMap<String, Integer> oldDataIndexes, Func1<T, String> keyGenerator) {

        List<AdapterUpdateData.DataRange> inserted = new ArrayList<>();

        int count = 0;

        int startIndex = 0;

        for (int i = 0; i < newData.size(); i++) {

            if (!oldDataIndexes.containsKey(keyGenerator.call(newData.get(i)))) {

                if (count == 0) {
                    startIndex = i;
                }

                count++;

            } else if (count > 0) {

                inserted.add(AdapterUpdateData.DataRange.create(startIndex, count));

                count = 0;
            }
        }

        if (count > 0) {

            inserted.add(AdapterUpdateData.DataRange.create(startIndex, count));
        }

        return inserted;
    }

    /**
     * Figure out which items were removed
     * from the list of data.
     */
    private List<AdapterUpdateData.DataRange> removeItems(List<T> oldData, LinkedHashMap<String, Integer> newDataIndexes, Func1<T, String> keyGenerator) {

        List<AdapterUpdateData.DataRange> removed = new ArrayList<>();

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

                removed.add(AdapterUpdateData.DataRange.create(startIndex, count));

                startIndexOffset -= count;

                count = 0;
            }
        }

        if (count > 0) {

            removed.add(AdapterUpdateData.DataRange.create(startIndex, count));
        }

        return removed;
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

    @AllArgsConstructor(staticName = "create")
    private static class AdapterUpdateData {

        @NonNull final List<Integer> indicesChanged;

        @NonNull final List<DataRange> indicesRangeRemoved;
        @NonNull final List<DataRange> indicesRangeInserted;

        public void update(RecyclerView.Adapter adapter) {

            for (int index : indicesChanged) {

                adapter.notifyItemChanged(index);
            }

            for (DataRange indexWithCount : indicesRangeRemoved) {

                adapter.notifyItemRangeRemoved(indexWithCount.index, indexWithCount.count);
            }

            for (DataRange indexWithCount : indicesRangeInserted) {

                adapter.notifyItemRangeInserted(indexWithCount.index, indexWithCount.count);
            }
        }

        @AllArgsConstructor(staticName = "create")
        private static class DataRange {

            final int index;
            final int count;
        }
    }
}
