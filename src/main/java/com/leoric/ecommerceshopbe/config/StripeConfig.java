package com.leoric.ecommerceshopbe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

//    @PostConstruct
//    public void setup() {
//        Stripe.apiKey = secretKey;
//    }
}
