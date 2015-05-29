package com.miguelgaeta.bootstrap.mg_rest;

import javax.net.ssl.SSLSocketFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by mrkcsc on 2/10/15.
 */
public class MGRestClientConfig {

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private RestAdapter.Log logging;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private boolean cookieStorageEnabled;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private String baseAPIURL;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private RequestInterceptor interceptor;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private int timeoutInSections = 10;

    @Setter @Getter(value = AccessLevel.PACKAGE)
    private SSLSocketFactory socketFactory;
}
