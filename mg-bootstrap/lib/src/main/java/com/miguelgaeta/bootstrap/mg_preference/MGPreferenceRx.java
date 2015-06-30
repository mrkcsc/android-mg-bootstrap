package com.miguelgaeta.bootstrap.mg_preference;

import com.google.gson.reflect.TypeToken;
import com.miguelgaeta.bootstrap.mg_rx.MGRxError;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

/**
 * Represents a data object that is automatically exposed as
 * an observable, a blocking getter of the latest or default
 * value and is persisted across subsequent launches via a cache.
 *
 * Created by Miguel Gaeta on 4/2/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGPreferenceRx<T> {

    /**
     * Persist value of this data into the
     * android preferences object.
     */
    @Getter(AccessLevel.PRIVATE)
    private MGPreference<T> dataCache;

    /**
     * Can be used by to publish
     * a new value to the data class.
     */
    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final SerializedSubject<T, T> dataPublisher = new SerializedSubject<>(BehaviorSubject.create());

    public static <T> MGPreferenceRx<T> create(String key, TypeToken<?> typeToken, T defaultValue, boolean cacheBreaker) {

        return new MGPreferenceRx<>(key, typeToken, defaultValue, cacheBreaker);
    }

    public static <T> MGPreferenceRx<T> create(String key, TypeToken<?> typeToken, T defaultValue) {

        return create(key, typeToken, defaultValue, true);
    }

    public static <T> MGPreferenceRx<T> create(String key, TypeToken<?> typeToken) {

        return create(key, typeToken, null);
    }

    /**
     * Initializes the preference object cache
     * and uses that to initialize rest of the
     * data object.
     */
    private MGPreferenceRx(String key, TypeToken<?> typeToken, T defaultValue, boolean cacheBreaker) {

        if (key != null) {

            // Initialize data cache.
            dataCache = new MGPreference<>(key, typeToken, defaultValue, cacheBreaker);
        }

        init(defaultValue);
    }

    /**
     * Set the data observable.
     */
    public void set(T t) {

        getDataPublisher().onNext(t);
    }

    public void merge(Func1<T, T> mergeFunction) {

        merge(mergeFunction, true);
    }

    public void merge(Func1<T, T> mergeFunction, boolean emitNull) {

        merge(mergeFunction, emitNull, true);
    }

    public void merge(Func1<T, T> mergeFunction, boolean emitNull, boolean mergeOnChange) {

        get(emitNull).take(1).subscribe(source -> {

            T mergedSource = mergeFunction.call(source);

            if (mergeOnChange) {

                if (source == null ? mergedSource != null : !source.equals(mergedSource)) {

                    set(mergedSource);
                }
            }

        }, MGRxError.create(null, "Unable to merge in new preference data."));
    }

    /**
     * Get the data publisher as a stream with a generic
     * buffer back-pressure strategy.
     */
    public Observable<T> get(boolean emitNull) {

        Observable<T> stream = getDataPublisher().onBackpressureBuffer();

        if (!emitNull) {

            return stream.filter(data -> data != null);
        }

        return stream;
    }

    /**
     * Get the data observable.
     */
    public Observable<T> get() {

        return get(true);
    }

    /**
     * Initialize the observable data stream
     * and if caching is enabled, set up
     * future value emissions.
     */
    private void init(T defaultValue) {

        get().subscribe(data -> {

            if (getDataCache() != null) {
                getDataCache().set(data);
            }

        }, MGRxError.create(null, "Unable to fetch initial preference data."));

        Observable.create(subscriber -> {

            // Cache lookup might take time, so do not block.
            set(getDataCache() != null ? getDataCache().get() : defaultValue);

        }).subscribeOn(MGPreferenceConfig.getScheduler()).subscribe(o -> {

        }, MGRxError.create(null, "Unable to set initial preference data."));
    }
}
