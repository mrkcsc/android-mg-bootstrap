package com.miguelgaeta.bootstrap.mg_ssl;

import android.content.res.AssetManager;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lombok.NonNull;

/**
 * Created by mrkcsc on 7/18/14.
 *
 * Reference: https://developer.android.com/training/articles/security-ssl.html
 */
public class MGSSLFactory {

    @SuppressWarnings("unused")
    public static TrustManagerFactory getTrustManager(@NonNull AssetManager assetManager, @NonNull String certificatePath) {

        try {

            final InputStream is = assetManager.open(certificatePath);

            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            final X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

            is.close();

            final TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

            final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null); // You don't need the KeyStore instance to come from a file.
            ks.setCertificateEntry("caCert", caCert);

            tmf.init(ks);

            return tmf;

        } catch (Exception e) {

            throw new RuntimeException("Unable to obtain an SSL context.", e);
        }
    }

    @SuppressWarnings("unused")
    public static SSLSocketFactory createSSLSocketFactory(boolean insecure, @Nullable TrustManagerFactory trustManagerFactory) {

        SSLContext sslContext = getSSLContext();

        try {

            if (insecure) {

                // Trust all certificates.
                sslContext.init(null, getInsecureTrustManager(), new java.security.SecureRandom());

            } else {

                // Trust device certificates, plus provided ones.
                sslContext.init(null, trustManagerFactory != null ? trustManagerFactory.getTrustManagers() : null, null);
            }

        } catch (KeyManagementException e) {

            throw new RuntimeException("Unable to initialize SSL context.", e);
        }

        return sslContext.getSocketFactory();
    }

    public static SSLSocketFactory createSSLSocketFactory(boolean insecure) {

        return createSSLSocketFactory(insecure, null);
    }

    @SuppressWarnings("unused")
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
