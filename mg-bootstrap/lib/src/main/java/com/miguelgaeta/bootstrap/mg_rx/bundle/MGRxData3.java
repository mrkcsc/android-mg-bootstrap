package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData3<A, B, C> {

    private A d1;
    private B d2;
    private C d3;

    public static <A, B, C> MGRxData3<A, B, C> create(A a, B b, C c) {

        return new MGRxData3<>(a, b, c);
    }
}
