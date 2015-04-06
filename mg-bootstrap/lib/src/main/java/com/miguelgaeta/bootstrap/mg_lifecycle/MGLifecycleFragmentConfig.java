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

    /**
     * Track if the helper lifecycle method createOrResume
     * has been invoked.  This is helpful when there is code
     * you want to either run in onCreate or subsequently
     * in the onResume lifecycle methods.
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private boolean onCreateOrResumeInvoked;
}
