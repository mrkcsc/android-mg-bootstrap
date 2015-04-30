package com.miguelgaeta.bootstrap.mg_preference;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
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
     * Back value of this data into the
     * android preferences object.
     */
    @Getter(AccessLevel.PRIVATE)
    private final MGPreference<T> dataCache;

    /**
     * Can be used by to publish
     * a new value to the data class.
     */
    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final SerializedSubject<T, T> dataPublisher = new SerializedSubject<>(BehaviorSubject.create());

    /**
     * Static initializer.
     */
    public static <T> MGPreferenceRx<T> create(String key) {

        return new MGPreferenceRx<>(key, null, true);
    }

    public static <T> MGPreferenceRx<T> create(String key, T defaultValue) {

        return new MGPreferenceRx<>(key, defaultValue, true);
    }

    public static <T> MGPreferenceRx<T> create(String key, T defaultValue, boolean cached) {

        return new MGPreferenceRx<>(key, defaultValue, cached);
    }

    /**
     * Initializes the preference object cache
     * and uses that to initialize rest of the
     * data object.
     */
    private MGPreferenceRx(String key, T defaultValue, boolean cached) {

        // Initialize data cache.
        dataCache = MGPreference.create(key, defaultValue);

        init(cached);
    }

    /**
     * Set the data observable.
     */
    public void set(T t) {

        getDataPublisher().onNext(t);
    }

    /**
     * Get the data observable, should it emit null values.
     */
    public Observable<T> get(boolean emitNull) {

        return emitNull ?
            getDataPublisher().asObservable() :
            getDataPublisher().filter(data -> data != null);
    }

    /**
     * Get the data observable.
     */
    public Observable<T> get() {

        return get(true);
    }

    /**
     * Get blocking version of the data observable.
     */
    public T getBlocking() {

        return getDataPublisher().toBlocking().mostRecent(null).iterator().next();
    }

    /**
     * Initialize the observable data stream
     * and if caching is enabled, set up
     * future value emissions.
     */
    private void init(boolean cached) {

        getDataPublisher().subscribe(data -> {

            if (cached) {

                dataCache.set(data);
            }
        });

        if (!cached) {

            // Clear existing cache.
            getDataCache().clear();
        }

        // Publish initial value.
        getDataPublisher().onNext(getDataCache().get());
    }
}
