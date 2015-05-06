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
    private List<Item> list = new ArrayList<>();

    /**
     * Add an item with an type for the adapter,
     * a key to distinguish it from other items,
     * and the item itself which can be any object.
     */
    public void add(int type, String key, Object item) {

        getList().add(Item.create(type, key, item));
    }

    @SuppressWarnings("UnusedDeclaration") @Getter @ToString @EqualsAndHashCode @AllArgsConstructor(staticName = "create")
    public static class Item {

        private int type;

        private String key;

        private Object item;

        public String getKey() {

            return type + "-" + key;
        }
    }
}
