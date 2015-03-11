package com.miguelgaeta.bootstrap.mg_rest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by mrkcsc on 7/18/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGRestClientSSL {

    /**
     * Creates a SSL socket factory that does
     * not validate certificates.
     */
    public static SSLSocketFactory createInsecureSSLSocketFactory() {

        return createInsecureSSLContext().getSocketFactory();
    }

    /**
     * Creates a socket factory that does not
     * validate certificates.
     */
    public static SSLContext createInsecureSSLContext() {

        // Create trust manager that does not validate certs chains.
        final TrustManager[] trustAllCerts = new TrustManager[] {

                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                }
        };

        SSLContext sslContext = null;

        try {

            // Install the all-trusting trust manager.
            sslContext = SSLContext.getInstance("SSL");

        } catch (NoSuchAlgorithmException ignored) { }

        try {

            if (sslContext != null) {
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            }
        } catch (KeyManagementException ignored) { }

        return sslContext;
    }
}
