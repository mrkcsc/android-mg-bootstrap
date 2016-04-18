package com.miguelgaeta.bootstrap.mg_preference;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

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
     * Use to buffer values that get set
     * before the preference is initialized.
     */
    @Getter(lazy = true, value = AccessLevel.PRIVATE)
    private final Buffer<T> buffer = new Buffer<>();

    private final String key;

    /**
     * Can be used by to publish
     * a new value to the data class.
     */
    @Getter(lazy = true, value = AccessLevel.PRIVATE) @SuppressWarnings("unchecked")
    private final SerializedSubject<T, T> dataPublisher = new SerializedSubject<>((Subject<T, T>) BehaviorSubject.create());

    public static <T> MGPreferenceRx<T> create(@Nullable String key, T defaultValue, int serializationDelay) {

        return new MGPreferenceRx<>(key, defaultValue, serializationDelay);
    }

    public static <T> MGPreferenceRx<T> create(@Nullable String key, T defaultValue) {

        return create(key, defaultValue, 100);
    }

    public static <T> MGPreferenceRx<T> create(@Nullable String key) {

        return create(key, null);
    }

    /**
     * Initialize the preference stream with a cached preference
     * or just use as a non-cached stream is no key is given.
     */
    private MGPreferenceRx(String key, T defaultValue, int serializationDelay) {

        this.key = key;

        init(defaultValue, key != null ? MGPreference.create(key, defaultValue, serializationDelay) : null);
    }

    /**
     * Set the data observable.
     */
    public void set(T t) {

        if (getBuffer().uninitialized) {
            getBuffer().items.add(t);

        } else {

            try {
                getDataPublisher().onNext(t);

            } catch (Exception e) {

                Log.e("MGPreference", "Unable to publish preference, most likely due to back-pressure. Key: " + key, e);
            }
        }
    }

    public void merge(Func1<T, T> mergeFunction) {

        merge(mergeFunction, true);
    }

    public void merge(Func1<T, T> mergeFunction, boolean emitNull) {

        merge(mergeFunction, emitNull, true);
    }

    public void merge(final Func1<T, T> mergeFunction, boolean emitNull, final boolean mergeOnChange) {

        get(emitNull).take(1).subscribe(new Action1<T>() {
            @Override
            public void call(T source) {
                final T mergedSource = mergeFunction.call(source);

                if (mergeOnChange) {

                    if (source == null ? mergedSource != null : !source.equals(mergedSource)) {

                        set(mergedSource);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                MGPreference
                    .getErrorHandler()
                    .call(new MGPreference.Error("Unable to merge in new preference data.", throwable));
            }
        });
    }

    /**
     * Get the data publisher as a stream with a generic
     * buffer back-pressure strategy.
     */
    public Observable<T> get(boolean emitNull) {

        Observable<T> stream = getDataPublisher().onBackpressureBuffer();

        if (!emitNull) {

            return stream.filter(new Func1<T, Boolean>() {
                @Override
                public Boolean call(final T data) {
                    return data != null;
                }
            });
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
    private void init(final T defaultValue, final MGPreference<T> cache) {

        if (cache != null) {

            get().observeOn(MGPreference.getScheduler()).subscribe(new Action1<T>() {
                @Override
                public void call(T t) {
                    cache.set(t);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    MGPreference
                        .getErrorHandler()
                        .call(new MGPreference.Error("Unable to cache " + key + " preference data.", throwable));
                }
            });
        }

        whenInitialized().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                setInitialized(cache != null ? cache.get() : defaultValue);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                MGPreference.getErrorHandler().call(
                    new MGPreference.Error("Unable to initialize " + key + " preference.", throwable));

                setInitialized(defaultValue);
            }
        });
    }

    /**
     * Initialize the first value and then flush
     * out any buffered values.
     */
    private void setInitialized(T value) {

        synchronized (getBuffer()) {

            getBuffer().uninitialized = false;

            set(value);

            for (T bufferedItem : getBuffer().items) {

                set(bufferedItem);
            }

            getBuffer().items.clear();
        }
    }

    private Observable<Boolean> whenInitialized() {

        return MGPreference.getInitialized().observeOn(MGPreference.getScheduler()).take(1);
    }

    private static class Buffer<T> {

        private boolean uninitialized = true;

        @SuppressWarnings("unchecked")
        private final List<T> items = (List<T>) Collections.synchronizedList(new ArrayList<>());
    }
}
