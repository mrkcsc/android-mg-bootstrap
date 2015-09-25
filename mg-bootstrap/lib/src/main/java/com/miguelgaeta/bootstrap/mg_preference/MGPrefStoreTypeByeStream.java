package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
public class MGPrefStoreTypeByeStream implements MGPrefStoreInterface {

    private Context context;

    @Getter(lazy = true)
    private final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {

        protected Kryo initialValue() {

            return new Kryo();
        }
    };

    @Override
    public void init(@NonNull Context context) {

        this.context = context;
    }

    @Override
    public Object get(@NonNull String key, Type typeOfObject, boolean versioned) {


        try {
            final Input input = new Input(context.openFileInput("shared_prefs_key.bin"));

            final Object output = getKryos().get().readClassAndObject(input);

            input.close();

            return output;

        } catch (FileNotFoundException e) {

            MGLog.i(e, "File not found for key: " + key);
        }

        return null;
    }

    @Override
    public void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned) {

        try {
            final Output output =
                new Output(context.openFileOutput("shared_prefs_key.bin", Context.MODE_PRIVATE));

            getKryos().get().writeClassAndObject(output, value);

            output.close();

        } catch (FileNotFoundException e) {

            MGLog.i(e, "File not found for key: " + key);
        }
    }

    @Override
    public void clear() {

        // TODO
    }
}
