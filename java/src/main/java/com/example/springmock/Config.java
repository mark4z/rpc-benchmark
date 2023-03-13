package com.example.springmock;

import com.example.springmock.grpc.SimpleServiceGrpc;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.apache.http.client.CredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


@Configuration
public class Config {
    @Value("${poll:1000}")
    private int poll;
    @Value("${grpcAddress:grpc:8081}")
    private String grpcAddress;

    @Bean
    public RestTemplate http2RestTemplate() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(poll, 5, TimeUnit.MINUTES))
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(okHttp3ClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(poll, 5, TimeUnit.MINUTES))
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        return new RestTemplate(okHttp3ClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate normalRestTemplate() throws Exception {
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        return new RestTemplate(okHttp3ClientHttpRequestFactory);
    }

    @Bean
    public SimpleServiceGrpc.SimpleServiceBlockingStub simpleService() {
        ManagedChannel channel = Grpc.newChannelBuilder(grpcAddress, InsecureChannelCredentials.create())
                .build();
        return SimpleServiceGrpc.newBlockingStub(channel);
    }
}
