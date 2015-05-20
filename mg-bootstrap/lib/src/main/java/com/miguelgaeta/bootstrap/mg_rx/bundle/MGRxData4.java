package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData4<A, B, C, D> {

    private A d1;
    private B d2;
    private C d3;
    private D d4;

    public static <A, B, C, D> MGRxData4<A, B, C, D> create(A a, B b, C c, D d) {

        return new MGRxData4<>(a, b, c, d);
    }
}
