package com.miguelgaeta.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.miguelgaeta.R;
import com.miguelgaeta.bootstrap.mg_log.MGLog;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapter;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerAdapterData;
import com.miguelgaeta.bootstrap.mg_recycler.MGRecyclerViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import rx.functions.Func1;

/**
 * Created by mrkcsc on 4/27/15.
 */
public class TestActivityRecyclerAdapter extends MGRecyclerAdapter {

    @Getter
    private MGRecyclerAdapterData<List<Integer>> data = MGRecyclerAdapterData.create(new ArrayList<>(), (oldData, newData) -> {

        Func1<Integer, String> keyGenerator = integer -> Integer.toString(integer);

        LinkedHashMap<String, Integer> oldDataIndexes = generateIndexMap(oldData, keyGenerator);
        LinkedHashMap<String, Integer> newDataIndexes = generateIndexMap(newData, keyGenerator);

        changeItems(oldDataIndexes, newDataIndexes);
        removeItems(oldData, newDataIndexes, keyGenerator);
        insertItems(newData, oldDataIndexes, keyGenerator);

        MGLog.e("Old: " + oldDataIndexes);
        MGLog.e("New: " + newDataIndexes);

        MGLog.e(".");
        MGLog.e("================================================");
        MGLog.e(".");
    });

    private void insertItems(List<Integer> newData, LinkedHashMap<String, Integer> oldDataIndexes, Func1<Integer, String> keyGenerator) {

        int count = 0;

        int startIndex = 0;

        for (int i = 0; i < newData.size(); i++) {

            if (!oldDataIndexes.containsKey(keyGenerator.call(newData.get(i)))) {

                if (count == 0) {
                    startIndex = i;
                }

                count++;

            } else if (count > 0) {

                MGLog.e("Inserted: " + count + " at: " + startIndex);

                notifyItemRangeInserted(startIndex, count);

                count = 0;
            }
        }

        if (count > 0) {

            MGLog.e("Inserted: " + count + " at: " + startIndex);

            notifyItemRangeRemoved(startIndex, count);
        }
    }

    private void removeItems(List<Integer> oldData, LinkedHashMap<String, Integer> newDataIndexes, Func1<Integer, String> keyGenerator) {

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

                MGLog.e("Removed: " + count + " at: " + startIndex);

                notifyItemRangeRemoved(startIndex, count);

                startIndexOffset -= count;

                count = 0;
            }
        }

        if (count > 0) {

            MGLog.e("Removed: " + count + " at: " + startIndex);

            notifyItemRangeRemoved(startIndex, count);
        }
    }

    private void changeItems(LinkedHashMap<String, Integer> oldDataIndexes, LinkedHashMap<String, Integer> newDataIndexes) {

        List<String> keys = new ArrayList<>(oldDataIndexes.keySet());

        for (int i = 0; i < oldDataIndexes.size(); i++) {

            String key = keys.get(i);

            if (newDataIndexes.containsKey(key)) {

                int oldIndex = oldDataIndexes.get(key);
                int newIndex = newDataIndexes.get(key);

                if (oldIndex != newIndex) {

                    MGLog.e("Item at: " + oldIndex + " changed.");

                    notifyItemChanged(oldIndex);
                }
            }
        }
    }

    /**
     * Given a list of data and a key generator function.  Create
     * a linked hash map of keys to associated index positions.
     */
    private static <T> LinkedHashMap<String, Integer> generateIndexMap(@NonNull List<T> data, Func1<T, String> keyGenerator) {

        LinkedHashMap<String, Integer> indexMap = new LinkedHashMap<>();

        for (int index = 0; index < data.size(); index++) {

            indexMap.put(keyGenerator.call(data.get(index)), index);
        }

        return indexMap;
    }

    public TestActivityRecyclerAdapter(@NonNull RecyclerView recycler) {
        super(recycler);
    }

    @Override
    public MGRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new TestActivityRecyclerViewHolder(R.layout.test_activity_recycler_item, this);
    }

    @Override
    public int getItemCount() {

        return data.getData().size();
    }
}
