package com.example.springmock;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {
    @Value("${address:https://http:8080}")
    private String address;

    private final RestTemplate restTemplate;
    private final RestTemplate http2RestTemplate;

    public Controller(RestTemplate http2RestTemplate, RestTemplate restTemplate) {
        this.http2RestTemplate = http2RestTemplate;
        this.restTemplate = restTemplate;
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
}
