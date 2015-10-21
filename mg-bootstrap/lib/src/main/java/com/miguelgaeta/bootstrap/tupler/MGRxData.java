package com.miguelgaeta.bootstrap.tupler;

import rx.Observable;

/**
 * Created by Miguel Gaeta on 10/20/15.
 */
@SuppressWarnings("unused")
public class MGRxData {

    public static <A> MGRxData1<A> create(A a) {
        return new MGRxData1<>(a);
    }

    public static <A, B> MGRxData2<A, B> create(A a, B b) {
        return new MGRxData2<>(a, b);
    }

    public static <A, B, C> MGRxData3<A, B, C> create(A a, B b, C c) {
        return new MGRxData3<>(a, b, c);
    }

    public static <A, B, C, D> MGRxData4<A, B, C, D> create(A a, B b, C c, D d) {
        return new MGRxData4<>(a, b, c, d);
    }

    public static <A, B, C, D, E> MGRxData5<A, B, C, D, E> create(A a, B b, C c, D d, E e) {
        return new MGRxData5<>(a, b, c, d, e);
    }

    public static <A, B, C, D, E, F> MGRxData6<A, B, C, D, E, F> create(A a, B b, C c, D d, E e, F f) {
        return new MGRxData6<>(a, b, c, d, e, f);
    }

    public static <A, B, C, D, E, F, G> MGRxData7<A, B, C, D, E, F, G> create(A a, B b, C c, D d, E e, F f, G g) {
        return new MGRxData7<>(a, b, c, d, e, f, g);
    }

    public static <A, B, C, D, E, F, G, H> MGRxData8<A, B, C, D, E, F, G, H> create(A a, B b, C c, D d, E e, F f, G g, H h) {
        return new MGRxData8<>(a, b, c, d, e, f, g, h);
    }

    public static <A, B, C, D, E, F, G, H, I> MGRxData9<A, B, C, D, E, F, G, H, I> create(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
        return new MGRxData9<>(a, b, c, d, e, f, g, h, i);
    }

    public static <A> Observable<MGRxData1<A>> combineLatest(Observable<A> a) {
        return a.map(MGRxData::create);
    }

    public static <A, B> Observable<MGRxData2<A, B>> combineLatest(Observable<A> a, Observable<B> b) {
        return Observable.combineLatest(a, b, MGRxData::create);
    }

    public static <A, B, C> Observable<MGRxData3<A, B, C>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c) {
        return Observable.combineLatest(a, b, c, MGRxData::create);
    }

    public static <A, B, C, D> Observable<MGRxData4<A, B, C, D>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d) {
        return Observable.combineLatest(a, b, c, d, MGRxData::create);
    }

    public static <A, B, C, D, E> Observable<MGRxData5<A, B, C, D, E>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e) {
        return Observable.combineLatest(a, b, c, d, e, MGRxData::create);
    }

    public static <A, B, C, D, E, F> Observable<MGRxData6<A, B, C, D, E, F>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f) {
        return Observable.combineLatest(a, b, c, d, e, f, MGRxData::create);
    }

    public static <A, B, C, D, E, F, G> Observable<MGRxData7<A, B, C, D, E, F, G>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g) {
        return Observable.combineLatest(a, b, c, d, e, f, g, MGRxData::create);
    }

    public static <A, B, C, D, E, F, G, H> Observable<MGRxData8<A, B, C, D, E, F, G, H>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g, Observable<H> h) {
        return Observable.combineLatest(a, b, c, d, e, f, g, h, MGRxData::create);
    }

    public static <A, B, C, D, E, F, G, H, I> Observable<MGRxData9<A, B, C, D, E, F, G, H, I>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g, Observable<H> h, Observable<I> i) {
        return Observable.combineLatest(a, b, c, d, e, f, g, h, i, MGRxData::create);
    }
}
