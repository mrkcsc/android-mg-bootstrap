package com.miguelgaeta.bootstrap.mg_rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import lombok.Getter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by mrkcsc on 2/10/15.
 */
@SuppressWarnings("UnusedDeclaration")
public class MGRestClient {

    @Getter(lazy = true)
    private final MGRestClientConfig config = new MGRestClientConfig();

    /**
     * Return standard rest adapter configured for
     * the platform project.
     */
    public RestAdapter getRestAdapter() {

        // Custom (better) http client.
        OkClient client = getHttpClient();

        // Configured endpoint.
        String endpoint = getConfig().getBaseAPIURL();

        // Handles date de/serialization.
        Converter converter = getGsonConverter();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setClient(client)
                .setEndpoint(endpoint)
                .setConverter(converter)
                .setRequestInterceptor(this::addAuthorizationHeader);

        if (getConfig().isLoggingEnabled()) {

            // Emit complete log level with a REST tag.
            builder.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("REST"));
        }

        return builder.build();
    }

    /**
     * Create a gson object configured to handle
     * JodaTime dates and any additional custom
     * configuration options that may be needed.
     */
    public static Gson getGson() {

        GsonBuilder gsonBuilder = new GsonBuilder();

        // Fetch ISO formatter.
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

        JsonSerializer<DateTime> typeAdapterSerialization = (sourceDate, typeOf, context) ->
                sourceDate == null ? null : new JsonPrimitive(dateTimeFormatter.print(sourceDate));

        JsonDeserializer<DateTime> typeAdapterDeserialization = (jsonDate, typeOf, context) ->
                jsonDate == null ? null : new DateTime(jsonDate.getAsString());

        // Support complex map keys.
        gsonBuilder.enableComplexMapKeySerialization();

        gsonBuilder.registerTypeAdapter(DateTime.class, typeAdapterSerialization);
        gsonBuilder.registerTypeAdapter(DateTime.class, typeAdapterDeserialization);

        return gsonBuilder.create();
    }

    private void addAuthorizationHeader(RequestInterceptor.RequestFacade request) {

        if (getConfig().getAuthorizationToken() != null) {
            request.addHeader("Authorization", getConfig().getAuthorizationToken());
        }
    }

    /**
     * Create a custom GSON converter that can
     * handle de/serialization of JodaTime date objects.
     */
    private GsonConverter getGsonConverter() {

        GsonBuilder gsonBuilder = new GsonBuilder();

        // Fetch ISO formatter.
        DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

        JsonSerializer<DateTime> typeAdapterSerialization = (sourceDate, typeOf, context) ->
                sourceDate == null ? null : new JsonPrimitive(dateTimeFormatter.print(sourceDate));

        JsonDeserializer<DateTime> typeAdapterDeserialization = (jsonDate, typeOf, context) ->
                jsonDate == null ? null : new DateTime(jsonDate.getAsString());

        gsonBuilder.registerTypeAdapter(DateTime.class, typeAdapterSerialization);
        gsonBuilder.registerTypeAdapter(DateTime.class, typeAdapterDeserialization);

        return new GsonConverter(getGson());
    }

    /**
     * Fetch custom HTTP client (configure it
     * to use a specified timeout).
     */
    private OkClient getHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient();

        // Setup hostname verifier.
        setupHostnameVerifier(okHttpClient);

        // Setup SSL.
        setupSSL(okHttpClient);

        okHttpClient.setConnectTimeout(getConfig().getTimeoutInSections(), TimeUnit.SECONDS);
        okHttpClient.setCookieHandler(getPersistentCookieManager());

        return new OkClient(okHttpClient);
    }

    /**
     * Create a custom verifier that does not
     * attempt to verify host names.
     *
     * @param okHttpClient Target client.
     */
    private void setupHostnameVerifier(OkHttpClient okHttpClient) {

        // Create a verifier that does not verify host-names.
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;

        // Assign it to the client.
        okHttpClient.setHostnameVerifier(hostnameVerifier);
    }

    /**
     * Setup a custom SSL that does not
     * validate certificates.
     *
     * @param okHttpClient Target client.
     */
    private void setupSSL(OkHttpClient okHttpClient) {

        // Create an ssl socket factory with our all-trusting manager.
        SSLSocketFactory sslSocketFactory = MGRestClientSSL.createInsecureSSLSocketFactory();

        if (sslSocketFactory != null) {

            // Assign it to the client.
            okHttpClient.setSslSocketFactory(sslSocketFactory);
        }
    }

    /**
     * Persist cookies across requests.
     */
    private CookieManager getPersistentCookieManager() {

        CookieManager cookieManager = new CookieManager();

        // Accept all cookies and persist them.
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        return cookieManager;
    }
}
