package com.example.springmock;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config {
    @Value("${poll:1000}")
    private int poll;

    @Bean
    public RestTemplate http2RestTemplate() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(poll, 1, TimeUnit.SECONDS))
                .protocols(Arrays.asList(Protocol.HTTP_1_1, Protocol.HTTP_2))
                .sslSocketFactory(getSSl().getSocketFactory())
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(okHttp3ClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(poll, 5, TimeUnit.MINUTES))
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .sslSocketFactory(getSSl().getSocketFactory())
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(okHttp3ClientHttpRequestFactory);
    }

    private SSLContext getSSl() throws Exception {
        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        //read the file from spring resources
        ClassPathResource classPathResource = new ClassPathResource("rootCA.pem");
        InputStream caInput = new BufferedInputStream(classPathResource.getInputStream());
        Certificate ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        caInput.close();
// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        return context;
    }
}
