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
class MGRestClientSSL {

    // region Public Methods
    // ============================================================================================================

    /**
     * Creates a socket factory that does not
     * validate and certificates.
     *
     * @return Socket factory, null on error.
     */
    @SuppressWarnings("unused")
    public static SSLSocketFactory createInsecureSSLSocketFactory() {

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


        if (sslContext != null) {

            // Create factory with all-trusting manager.
            return sslContext.getSocketFactory();
        }

        // Error.
        return null;
    }

    // endregion
}
