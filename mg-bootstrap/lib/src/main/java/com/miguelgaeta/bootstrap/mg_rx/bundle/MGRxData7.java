package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData7<A, B, C, D, E, F, G> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;
    public E d5;
    public F d6;
    public G d7;

    public static <A, B, C, D, E, F, G> MGRxData7<A, B, C, D, E, F, G> create(A a, B b, C c, D d, E e, F f, G g) {

        return new MGRxData7<>(a, b, c, d, e, f, g);
    }

    public static <A, B, C, D, E, F, G> Observable<MGRxData7<A, B, C, D, E, F, G>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f, Observable<G> g) {

        return Observable.combineLatest(a, b, c, d, e, f, g, MGRxData7::create);
    }
}