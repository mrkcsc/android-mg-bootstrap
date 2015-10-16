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
public class MGRxData2<A, B> {

    public A d1;
    public B d2;

    public static <A, B> MGRxData2<A, B> create(A a, B b) {

        return new MGRxData2<>(a, b);
    }

    public static <A, B> Observable<MGRxData2<A, B>> combineLatest(Observable<A> a, Observable<B> b) {

        return Observable.combineLatest(a, b, MGRxData2::create);
    }
}
