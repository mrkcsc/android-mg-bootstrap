package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration") @ToString @EqualsAndHashCode
public class MGRxData6<A, B, C, D, E, F> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;
    public E d5;
    public F d6;

    public static <A, B, C, D, E, F> MGRxData6<A, B, C, D, E, F> create(A a, B b, C c, D d, E e, F f) {

        return new MGRxData6<>(a, b, c, d, e, f);
    }

    public static <A, B, C, D, E, F> Observable<MGRxData6<A, B, C, D, E, F>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e, Observable<F> f) {

        return Observable.combineLatest(a, b, c, d, e, f, MGRxData6::create);
    }
}