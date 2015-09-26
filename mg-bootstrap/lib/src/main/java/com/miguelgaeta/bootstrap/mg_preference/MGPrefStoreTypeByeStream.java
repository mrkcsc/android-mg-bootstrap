package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
class MGPrefStoreTypeByeStream extends MGPreferenceStore {

    @Getter(lazy = true)
    private final Context context = MGLifecycleApplication.getContext();

    private ThreadLocal<Kryo> kryos;

    @Override
    public void init(@NonNull Context context) {

    }

    @Override
    public Object get(@NonNull String key, Type typeOfObject, boolean versioned) {

        final File file = new File(getContext().getFilesDir() + "/" + getFileName(0, key));

        if (file.exists()) {

            try {

                final Input input = new Input(new FileInputStream(file));

                final Object output = getKryo().readClassAndObject(input);

                input.close();

                return output;

            } catch (KryoException | FileNotFoundException e) {

                MGLog.i(e, "Unable to deserialize for key: " + key);
            }
        }

        return null;
    }

    @Override
    public void set(@NonNull String key, Object value, Type typeOfObject, boolean versioned) {

        try {
            final Output output = new Output(getContext().openFileOutput(getFileName(0, key), Context.MODE_PRIVATE));

            getKryo().writeClassAndObject(output, value);

            output.close();

        } catch (KryoException | FileNotFoundException e) {

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

                    // Use the default instantiation, but attempt to use JVM trickery if that fails.
                    kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

                    return kryo;
                }
            };
        }

        return kryos.get();
    }
}
