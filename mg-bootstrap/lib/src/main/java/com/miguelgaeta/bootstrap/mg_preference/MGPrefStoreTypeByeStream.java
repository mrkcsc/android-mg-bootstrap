package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
public class MGPrefStoreTypeByeStream extends MGPreferenceStore {

    private Context context;

    private ThreadLocal<Kryo> kryos;

    private boolean allowClassesWithNoConstructors;

    public void init(@NonNull Context context, boolean allowClassesWithNoConstructors) {

        this.context = context;

        this.allowClassesWithNoConstructors = allowClassesWithNoConstructors;
    }

    @Override
    public void init(@NonNull Context context) {

        init(context, true);
    }

    @Override
    public Object get(@NonNull String key, Type typeOfObject, boolean versioned) {

        try {

            final Input input = new Input(context.openFileInput("shared_prefs_key.bin"));

            final Object output = getKryo().readClassAndObject(input);

            input.close();

            return output;

        } catch (KryoException | FileNotFoundException e) {

            MGLog.i(e, "Unable to deserialize for key: " + key);
        }

        return null;
    }

    @Override
    public void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned) {

        try {
            final Output output =
                new Output(context.openFileOutput("shared_prefs_key.bin", Context.MODE_PRIVATE));

            getKryo().writeClassAndObject(output, value);

            output.close();

        } catch (FileNotFoundException e) {

            MGLog.i(e, "File not found for key: " + key);
        }
    }

    @Override
    public void clear() {

        // TODO
    }

    private Kryo getKryo() {

        if (kryos == null) {
            kryos = new ThreadLocal<Kryo>() {

                protected Kryo initialValue() {

                    final Kryo kryo = new Kryo();

                    if (allowClassesWithNoConstructors) {

                        // Use the default instantiation, but attempt to use JVM trickery if that fails.
                        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
                    }

                    return kryo;
                }
            };
        }

        return kryos.get();
    }
}
