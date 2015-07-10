package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration") @ToString @EqualsAndHashCode
public class MGRxData9<A, B, C, D, E, F, G, H, I> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;
    public E d5;
    public F d6;
    public G d7;
    public H d8;
    public I d9;

    public static <A, B, C, D, E, F, G, H, I> MGRxData9<A, B, C, D, E, F, G, H, I> create(A a, B b, C c, D d, E e, F f, G g, H h, I i) {

        return new MGRxData9<>(a, b, c, d, e, f, g, h, i);
    }

    public static <A, B, C, D, E, F, G, H, I> Observable<MGRxData9<A, B, C, D, E, F, G, H, I>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g, Observable<H> h, Observable<I> i) {

        return Observable.combineLatest(a, b, c, d, e, f, g, h, i, MGRxData9::create);
    }
}