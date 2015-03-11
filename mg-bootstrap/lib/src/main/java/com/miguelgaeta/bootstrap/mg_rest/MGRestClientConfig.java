package com.miguelgaeta.bootstrap.mg_rest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by mrkcsc on 2/10/15.
 */
public class MGRestClientConfig {

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private boolean loggingEnabled;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private String baseAPIURL;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private String authorizationToken;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private int timeoutInSections = 10;
}
