package com.example.springmock;

import com.example.springmock.grpc.SimpleRequest;
import com.example.springmock.grpc.SimpleRequestOrBuilder;
import com.example.springmock.grpc.SimpleServiceGrpc;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    @Value("${address:https://http.local:8080}")
    private String address;
    private final RestTemplate restTemplate;
    private final RestTemplate http2RestTemplate;
    private final RestTemplate normalRestTemplate;
    private final SimpleServiceGrpc.SimpleServiceBlockingStub simpleService;

    public Controller(RestTemplate http2RestTemplate, RestTemplate restTemplate, RestTemplate normalRestTemplate, SimpleServiceGrpc.SimpleServiceBlockingStub simpleService) {
        this.http2RestTemplate = http2RestTemplate;
        this.restTemplate = restTemplate;
        this.normalRestTemplate = normalRestTemplate;
        this.simpleService = simpleService;
    }

    @GetMapping("/")
    public String normal() {
        return normalRestTemplate.postForObject(address, "{}", String.class);
    }

    @GetMapping("/http1/{size}")
    public String http1(@PathVariable("size") int size) {
        return restTemplate.postForObject(address,
                RandomStringUtils.random(size), String.class);
    }

    @GetMapping("/http2/{size}")
    public String http2(@PathVariable("size") int size) {
        return http2RestTemplate.postForObject(address,
                RandomStringUtils.random(size), String.class);
    }

    @GetMapping("/grpc/{size}")
    public String grpc(@PathVariable("size") int size) {
        return simpleService.simpleRpc(
                SimpleRequest.newBuilder()
                        .setMessage(RandomStringUtils.random(size))
                        .build()).getMessage();
    }

    @GetMapping("/nothing")
    public String nothing() {
        return "nothing";
    }
}
