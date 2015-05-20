package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData5<A, B, C, D, E> {

    private A d1;
    private B d2;
    private C d3;
    private D d4;
    private E d5;

    public static <A, B, C, D, E> MGRxData5<A, B, C, D, E> create(A a, B b, C c, D d, E e) {

        return new MGRxData5<>(a, b, c, d, e);
    }
}