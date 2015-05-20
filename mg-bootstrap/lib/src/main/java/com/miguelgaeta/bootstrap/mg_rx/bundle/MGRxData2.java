package com.miguelgaeta.bootstrap.mg_rx.bundle;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@Getter @AllArgsConstructor @SuppressWarnings("UnusedDeclaration")
public class MGRxData2<A, B> {

    private A d1;
    private B d2;

    public static <A, B> MGRxData2<A, B> create(A a, B b) {

        return new MGRxData2<>(a, b);
    }
}
