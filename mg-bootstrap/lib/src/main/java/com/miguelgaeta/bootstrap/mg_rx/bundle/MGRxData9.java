package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData9<A, B, C, D, E, F, G, H, I> {

    private A d1;
    private B d2;
    private C d3;
    private D d4;
    private E d5;
    private F d6;
    private G d7;
    private H d8;
    private I d9;

    public static <A, B, C, D, E, F, G, H, I> MGRxData9<A, B, C, D, E, F, G, H, I> create(A a, B b, C c, D d, E e, F f, G g, H h, I i) {

        return new MGRxData9<>(a, b, c, d, e, f, g, h, i);
    }
}