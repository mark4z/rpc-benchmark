package com.example.springmock;

import org.conscrypt.Conscrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;
import java.util.Locale;

@SpringBootApplication
public class SpringMockApplication {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "US"));
        Security.insertProviderAt(Conscrypt.newProvider(), 1);
        SpringApplication.run(SpringMockApplication.class, args);
    }

}
