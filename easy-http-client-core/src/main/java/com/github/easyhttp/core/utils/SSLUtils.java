package com.github.easyhttp.core.utils;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/5/22 22:27
 */
public class SSLUtils {

    private static TrustManager[] trustAllHttpsCertificates() throws KeyManagementException {

        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        return trustManagers;
    }

    public static CloseableHttpClient createHttpClientSSL() throws NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = null;
        TrustManager[] trustManagers = trustAllHttpsCertificates();
        SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.SSL);
        sslContext.init(null, trustManagers, new SecureRandom());
        httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext)).build();
        return httpClient;
    }

    public static CloseableHttpClient createHttpClientTLS() throws NoSuchAlgorithmException, KeyManagementException {
        CloseableHttpClient httpClient = null;
        TrustManager[] trustManagers = trustAllHttpsCertificates();
        SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
        sslContext.init(null, trustManagers, new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpClient;
    }


}
