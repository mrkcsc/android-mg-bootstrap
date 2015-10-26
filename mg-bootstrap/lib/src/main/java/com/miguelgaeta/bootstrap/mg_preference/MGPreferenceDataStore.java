package com.miguelgaeta.bootstrap.mg_preference;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.miguelgaeta.bootstrap.mg_lifecycle.MGLifecycleApplication;
import com.miguelgaeta.bootstrap.mg_log.MGLog;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateSerializer;
import de.javakaffee.kryoserializers.jodatime.JodaLocalDateTimeSerializer;
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

        final long startTime = DateTime.now().getMillis();

        final File file = new File(getContext().getFilesDir() + "/" + getFileName(key));

        Object output = null;

        if (file.exists()) {

            try {

                final Input input = new Input(new FileInputStream(file));

                output = getKryo().readClassAndObject(input);

                input.close();

            } catch (KryoException | FileNotFoundException e) {

                MGLog.i(e, "Unable to deserialize for key: " + key);
            }
        }

        final long endTime = DateTime.now().getMillis();

        debugOperationTime("De-serialized", key, startTime, endTime);

        return output;
    }

    public void set(@NonNull String key, Object value) {

        try {

            final long startTime = DateTime.now().getMillis();

            final Output output = new Output(getContext().openFileOutput(getFileName(key), Context.MODE_PRIVATE));

            getKryo().writeClassAndObject(output, value);

            output.close();

            final long endTime = DateTime.now().getMillis();

            debugOperationTime("Serialized", key, startTime, endTime);

        } catch (KryoException | FileNotFoundException e) {

            MGLog.i(e, "File not found for key: " + key);
        }
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

                    // Support for serializing joda time.
                    kryo.register(DateTime.class, new JodaDateTimeSerializer());
                    kryo.register(LocalDate.class, new JodaLocalDateSerializer());
                    kryo.register(LocalDateTime.class, new JodaLocalDateTimeSerializer());

                    return kryo;
                }
            };
        }

        return kryos.get();
    }

    static String getFileName(String key) {

        return DATA_PREFIX + key;
    }

    private void debugOperationTime(String operation, String key, long startTime, long endTime) {

        final long time = endTime - startTime;

        if (time > 500) {

            MGLog.i(operation + " " + key + " in " + (time / 1000.0f) + " seconds");
        }
    }
}
