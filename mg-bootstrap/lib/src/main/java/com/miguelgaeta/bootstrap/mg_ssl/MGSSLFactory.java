package com.miguelgaeta.bootstrap.mg_ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by mrkcsc on 7/18/14.
 *
 * Reference: https://developer.android.com/training/articles/security-ssl.html
 */
public class MGSSLFactory {

    /**
     * Create an SSL socket factory.  If desired can be
     * set to insecure which allows all certificates.
     */
    public static SSLSocketFactory createSSLSocketFactory(boolean insecure) {

        SSLContext sslContext = getSSLContext();

        try {

            if (insecure) {

                // Trust all certificates.
                sslContext.init(null, getInsecureTrustManager(), new java.security.SecureRandom());

            } else {

                // Use default ones.
                sslContext.init(null, null, null);
            }

        } catch (KeyManagementException e) {

            throw new RuntimeException("Unable to initialize SSL context.", e);
        }

        return sslContext.getSocketFactory();
    }

    /**
     * Create an SSL socket factory.
     */
    public static SSLSocketFactory createSSLSocketFactory() {

        return createSSLSocketFactory(false);
    }

    /**
     * Create an SSL context.  Use the TLS
     * protocol as it is proffered.
     */
    private static SSLContext getSSLContext() {

        try {

            return SSLContext.getInstance("TLS");

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Unable to obtain an SSL context.", e);
        }
    }

    /**
     * This is generally not a good idea because it means
     * that the device will now trust all SSL
     * certificates leaving the device open to man
     * in the middle attacks.
     */
    private static TrustManager[] getInsecureTrustManager() {

        return new TrustManager[] {

            new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            }
        };
    }
}
