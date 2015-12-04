package com.miguelgaeta.bootstrap.mg_recycler;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Miguel Gaeta on 5/6/15.
 */
@SuppressWarnings("unused")
public class MGRecyclerDataPayload {

    @Getter
    private List<Contract> list = new ArrayList<>();

    public void add(int type, String key, Object item) {

        list.add(Item.create(type, key, item));
    }

    public void add(Integer position, int type, String key, Object item) {

        Item itemObject = Item.create(type, key, item);

        if (position == null) {

            // Insert at end,
            list.add(itemObject);

        } else {

            // Insert at position.
            list.add(position, itemObject);
        }
    }

    public Contract get(int position) {

        return list.get(position);
    }

    public int size() {

        return list.size();
    }

    @SuppressWarnings("UnusedDeclaration") @Getter @ToString @EqualsAndHashCode @AllArgsConstructor(staticName = "create")
    public static class Item implements Contract {

        private int type;

        private String key;

        private Object item;

        public String getKey() {

            return type + "-" + key;
        }
    }

    public interface Contract {

        int getType();

        String getKey();

        Object getItem();
    }
}
