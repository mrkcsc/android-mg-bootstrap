package com.miguelgaeta.bootstrap.mg_lifecycle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Miguel on 1/23/2015. Copyright 2014 Blitz Studios
 */
public class MGLifecycleFragmentConfig {

    /**
     * Track if this fragment is being created
     * for the first time.  This can be important
     * for activities that use fragments since
     * fragments do not get destroyed when activities
     * do so you should only add them once.
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean recreated;
}
