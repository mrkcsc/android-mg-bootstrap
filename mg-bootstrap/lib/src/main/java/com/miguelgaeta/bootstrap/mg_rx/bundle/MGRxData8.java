package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@SuppressWarnings("UnusedDeclaration") @AllArgsConstructor @NoArgsConstructor @ToString @EqualsAndHashCode
public class MGRxData8<A, B, C, D, E, F, G, H> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;
    public E d5;
    public F d6;
    public G d7;
    public H d8;

    public static <A, B, C, D, E, F, G, H> MGRxData8<A, B, C, D, E, F, G, H> create(A a, B b, C c, D d, E e, F f, G g, H h) {

        return new MGRxData8<>(a, b, c, d, e, f, g, h);
    }

    public static <A, B, C, D, E, F, G, H> Observable<MGRxData8<A, B, C, D, E, F, G, H>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g, Observable<H> h) {

        return Observable.combineLatest(a, b, c, d, e, f, g, h, MGRxData8::create);
    }
}