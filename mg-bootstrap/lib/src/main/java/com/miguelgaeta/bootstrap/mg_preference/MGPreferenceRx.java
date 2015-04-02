package com.miguelgaeta.bootstrap.mg_preference;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;

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
    @Getter(lazy = true, value = AccessLevel.PACKAGE)
    private final PublishSubject<T> dataPublisher = PublishSubject.create();

    /**
     * Observable of the data that emits whatever
     * the most recent published value is.
     */
    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final Observable<T> data = getDataPublisher().cache(1);

    /**
     * Static initializer.
     */
    public static <T> MGPreferenceRx<T> create(String key) {

        return new MGPreferenceRx<>(key);
    }

    /**
     * Initializes the preference object cache
     * and uses that to initialize rest of the
     * data object.
     */
    private MGPreferenceRx(String key) {

        // Initialize data cache.
        dataCache = MGPreference.create(key);

        init();
    }

    /**
     * Get the data observable.
     */
    public Observable<T> get() {

        return getData();
    }

    /**
     * Get a blocking version of the data observable.  Because it is
     * blocking, will return a default value if no previous
     * value has been set or returned from cache.
     */
    public T getBlocking() {

        return getData().toBlocking().mostRecent(null).iterator().next();
    }

    private void init() {

        getData().subscribe(d -> {

            // If value has changed from cache.
            if (getDataCache().get() != d) {

                // Persist.
                dataCache.set(d);
            }
        });

        if (getDataCache().get() != null) {

            // Publish initial value.
            getDataPublisher().onNext(getDataCache().get());
        }
    }
}
