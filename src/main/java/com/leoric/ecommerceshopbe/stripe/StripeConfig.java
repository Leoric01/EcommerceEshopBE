package com.leoric.ecommerceshopbe.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "stripe")
public class StripeConfig {
    private String publicKey;
    private String secretKey;
    private String endpointOnSuccess;
    private String endpointOnCancel;

    @PostConstruct
    public void setup() {
        Stripe.apiKey = secretKey;
    }

}
