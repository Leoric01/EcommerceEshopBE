package com.leoric.ecommerceshopbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ECommerceShopBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceShopBeApplication.class, args);
    }

}
