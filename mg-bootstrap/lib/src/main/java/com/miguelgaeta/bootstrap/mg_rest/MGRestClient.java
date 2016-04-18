package com.miguelgaeta.bootstrap.mg_rest;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import lombok.Getter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by mrkcsc on 2/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGRestClient {

    @Getter(lazy = true)
    private final MGRestClientConfig config = new MGRestClientConfig();

    @Getter(lazy = true)
    private final OkHttpClient okHttpClient = getHttpClient();

    /**
     * Return standard rest adapter configured for
     * the platform project.
     */
    public RestAdapter getRestAdapter() {

        // Configured endpoint.
        String endpoint = getConfig().getBaseAPIURL();

        RestAdapter.Builder builder = new RestAdapter.Builder()
            .setClient(new OkClient(getOkHttpClient())).setEndpoint(endpoint);

        builder = builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if (getConfig().getInterceptor() != null) {
                    getConfig().getInterceptor().intercept(request);
                }
            }
        });

        if (getConfig().getLogging() != null) {

            // Emit complete log level with a REST tag.
            builder = builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(getConfig().getLogging());
        }

        return builder.build();
    }

    /**
     * Create a gson object configured to handle
     * JodaTime dates and any additional custom
     * configuration options that may be needed.
     */
    public static Gson getGson() {

        return new GsonBuilder().create();
    }

    /**
     * Fetch custom HTTP client (configure it
     * to use a specified timeout).
     */
    private OkHttpClient getHttpClient() {

        OkHttpClient client = new OkHttpClient();

        // Setup hostname verifier.
        setupHostnameVerifier(client);

        if (getConfig().getSocketFactory() != null) {

            // Assign it to the client.
            client.setSslSocketFactory(getConfig().getSocketFactory());
        }

        if (getConfig().isCookieStorageEnabled()) {

            // Set a custom cookie handler that persists cookies onto the device.
            client.setCookieHandler(new CookieManager(new MGRestClientCookieStore(), CookiePolicy.ACCEPT_ALL));
        }

        return client;
    }

    /**
     * Create a custom verifier that does not
     * attempt to verify host names.
     *
     * @param okHttpClient Target client.
     */
    private void setupHostnameVerifier(OkHttpClient okHttpClient) {

        // Create a verifier that does not verify host-names.
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override @SuppressLint("BadHostnameVerifier")
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Assign it to the client.
        okHttpClient.setHostnameVerifier(hostnameVerifier);
    }
}
