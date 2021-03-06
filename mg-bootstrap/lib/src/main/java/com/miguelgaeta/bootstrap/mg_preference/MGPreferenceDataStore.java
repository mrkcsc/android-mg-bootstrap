package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miguelgaeta.bootstrap.logger.Logger;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;

import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import lombok.Getter;
import lombok.NonNull;

/**
 * Created by Miguel Gaeta on 9/25/15.
 */
class MGPreferenceDataStore {

    private static final String DATA_PREFIX = "PREF_K_";

    @Getter(lazy = true)
    private final Context context = MGLifecycleApplication.getContext();

    private ThreadLocal<Kryo> kryos;

    public Object get(@NonNull String key) {

        final Logger.Elapsed elapsed = Logger.Elapsed.create();

        final File file = new File(getContext().getFilesDir() + "/" + getFileName(key));

        Object output = null;

        if (file.exists()) {

            try {

                final Input input = new Input(new FileInputStream(file));

                output = getKryo().readClassAndObject(input);

                input.close();

            } catch (KryoException | FileNotFoundException e) {

                Log.i("MGPreference", "Unable to deserialize for key: " + key, e);
            }
        }

        debugOperationTime("De-serialized", key, elapsed);

        return output;
    }

    public void set(@NonNull String key, Object value) {

        final Logger.Elapsed elapsed = Logger.Elapsed.create();

        try {

            final Output output = new Output(getContext().openFileOutput(getFileName(key), Context.MODE_PRIVATE));

            getKryo().writeClassAndObject(output, value);

            output.close();

        } catch (KryoException | FileNotFoundException e) {

            Log.i("MGPreference", "File not found for key: " + key, e);
        }

        debugOperationTime("Serialized", key, elapsed);
    }

    public void reset() {

        for (String fileName : getContext().fileList()) {

            if (fileName.contains(DATA_PREFIX)) {

                getContext().deleteFile(fileName);
            }
        }
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

    static String getFileName(String key) {

        return DATA_PREFIX + key;
    }

    private void debugOperationTime(final String operation, String key, final Logger.Elapsed elapsed) {

        if (elapsed.getMilliseconds() > 500) {

            Log.i("MGPreference", operation + " " + key + " in " + elapsed.getSeconds() + " seconds");
        }
    }
}
