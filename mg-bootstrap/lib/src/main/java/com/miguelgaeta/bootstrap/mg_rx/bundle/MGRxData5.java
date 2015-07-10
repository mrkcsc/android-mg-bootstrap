package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration") @ToString @EqualsAndHashCode
public class MGRxData5<A, B, C, D, E> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;
    public E d5;

    public static <A, B, C, D, E> MGRxData5<A, B, C, D, E> create(A a, B b, C c, D d, E e) {

        return new MGRxData5<>(a, b, c, d, e);
    }

    public static <A, B, C, D, E> Observable<MGRxData5<A, B, C, D, E>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d, Observable<E> e) {

        return Observable.combineLatest(a, b, c, d, e, MGRxData5::create);
    }
}