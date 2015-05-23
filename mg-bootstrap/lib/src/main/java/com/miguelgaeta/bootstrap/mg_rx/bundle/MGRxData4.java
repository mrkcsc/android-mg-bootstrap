package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData4<A, B, C, D> {

    public A d1;
    public B d2;
    public C d3;
    public D d4;

    public static <A, B, C, D> MGRxData4<A, B, C, D> create(A a, B b, C c, D d) {

        return new MGRxData4<>(a, b, c, d);
    }

    public static <A, B, C, D> Observable<MGRxData4<A, B, C, D>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c, Observable<D> d) {

        return Observable.combineLatest(a, b, c, d, MGRxData4::create);
    }
}
