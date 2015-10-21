package com.miguelgaeta.bootstrap.tupler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Miguel Gaeta on 5/20/15.
 */
@SuppressWarnings("UnusedDeclaration") @AllArgsConstructor(access = AccessLevel.PACKAGE) @ToString @EqualsAndHashCode
public class Tuple6<A, B, C, D, E, F> {

    public final A d1;
    public final B d2;
    public final C d3;
    public final D d4;
    public final E d5;
    public final F d6;
}