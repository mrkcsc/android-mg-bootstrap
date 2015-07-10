package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import rx.Observable;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@AllArgsConstructor @SuppressWarnings("UnusedDeclaration") @ToString @EqualsAndHashCode
public class MGRxData3<A, B, C> {

    public A d1;
    public B d2;
    public C d3;

    public static <A, B, C> MGRxData3<A, B, C> create(A a, B b, C c) {

        return new MGRxData3<>(a, b, c);
    }

    public static <A, B, C> Observable<MGRxData3<A, B, C>> combineLatest(Observable<A> a, Observable<B> b, Observable<C> c) {

        return Observable.combineLatest(a, b, c, MGRxData3::create);
    }
}
