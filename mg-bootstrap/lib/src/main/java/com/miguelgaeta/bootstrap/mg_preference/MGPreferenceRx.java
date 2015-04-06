package com.miguelgaeta.bootstrap.mg_preference;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.subjects.BehaviorSubject;

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
    private final BehaviorSubject<T> dataPublisher = BehaviorSubject.create();

    // If disabled, no not cache to disk.
    private final boolean dataCacheEnabled;

    /**
     * Static initializer.
     */
    public static <T> MGPreferenceRx<T> create(String key) {

        return new MGPreferenceRx<>(key, true);
    }

    /**
     * Static initializer, cached flag.
     */
    public static <T> MGPreferenceRx<T> create(String key, boolean cached) {

        return new MGPreferenceRx<>(key, cached);
    }

    /**
     * Initializes the preference object cache
     * and uses that to initialize rest of the
     * data object.
     */
    private MGPreferenceRx(String key, boolean cached) {

        // Initialize data cache.
        dataCache = MGPreference.create(key);
        dataCacheEnabled = cached;

        init();
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
     * Get blocking version of the data observable. Provide
     * a default value if none is found.
     */
    public T getBlocking(T defaultValue) {

        return getDataPublisher().toBlocking().mostRecent(defaultValue).iterator().next();
    }

    public T getBlocking() {

        return getBlocking(null);
    }

    private void init() {

        getDataPublisher().subscribe(data -> {

            if (dataCacheEnabled) {
                dataCache.set(data);
            }
        });

        // If a cached value exists (and caching enabled).
        if (dataCacheEnabled && getDataCache().get() != null) {

            // Publish initial value.
            getDataPublisher().onNext(getDataCache().get());
        }
    }
}
