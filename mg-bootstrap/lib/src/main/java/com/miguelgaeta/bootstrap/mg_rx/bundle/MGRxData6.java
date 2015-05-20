package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData6<A, B, C, D, E, F> {

    private A d1;
    private B d2;
    private C d3;
    private D d4;
    private E d5;
    private F d6;

    public static <A, B, C, D, E, F> MGRxData6<A, B, C, D, E, F> create(A a, B b, C c, D d, E e, F f) {

        return new MGRxData6<>(a, b, c, d, e, f);
    }
}