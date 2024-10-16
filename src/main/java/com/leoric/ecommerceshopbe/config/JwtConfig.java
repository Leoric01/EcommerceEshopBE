package com.leoric.ecommerceshopbe.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public static String SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;  // Set the static variable
    }
}
