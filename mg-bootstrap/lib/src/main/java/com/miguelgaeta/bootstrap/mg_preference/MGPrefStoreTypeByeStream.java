package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;

import java.lang.reflect.Type;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
public class MGPrefStoreTypeByeStream implements MGPrefStoreInterface {

    @Getter(lazy = true)
    private final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {

        protected Kryo initialValue() {

            return new Kryo();
        }
    };

    @Override
    public void init(@NonNull Context context) {

    }

    @Override
    public Object get(@NonNull String key, Type typeOfObject, boolean versioned) {

        return null;
    }

    @Override
    public void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned) {

    }

    @Override
    public void clear() {

    }
}
