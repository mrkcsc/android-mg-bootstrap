package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData8<A, B, C, D, E, F, G, H> {

    private A d1;
    private B d2;
    private C d3;
    private D d4;
    private E d5;
    private F d6;
    private G d7;
    private H d8;

    public static <A, B, C, D, E, F, G, H> MGRxData8<A, B, C, D, E, F, G, H> create(A a, B b, C c, D d, E e, F f, G g, H h) {

        return new MGRxData8<>(a, b, c, d, e, f, g, h);
    }
}