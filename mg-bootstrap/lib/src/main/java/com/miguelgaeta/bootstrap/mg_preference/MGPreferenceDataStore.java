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

    private static final String DATA_PREFIX = "PREF";

    @Getter(lazy = true)
    private final Context context = MGLifecycleApplication.getContext();

    private ThreadLocal<Kryo> kryos;

    public Object get(@NonNull String key) {

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

    public void set(@NonNull String key, Object value) {

        try {
            final Output output = new Output(getContext().openFileOutput(getFileName(0, key), Context.MODE_PRIVATE));

            getKryo().writeClassAndObject(output, value);

            output.close();

        } catch (KryoException | FileNotFoundException e) {

            MGLog.i(e, "File not found for key: " + key);
        }
    }

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

    static String getFileName(int versionCode, String key) {

        return DATA_PREFIX + "_V_" + versionCode + "_K_" + key;
    }
}
