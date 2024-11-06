package com.leoric.ecommerceshopbe;

import com.leoric.ecommerceshopbe.utils.datainit.DatabaseInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class ECommerceShopBeApplication {

    private final DatabaseInitializer databaseInitializer;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceShopBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return this::run;
    }

    private void run(String... args) {
        databaseInitializer.initUsers(5, 2);
        databaseInitializer.initAdmins(2);
        databaseInitializer.initSellers(6);
        databaseInitializer.initHomeCategories();
        databaseInitializer.initDeals();
        databaseInitializer.initCreateCoupons();
        databaseInitializer.initCategories();
        databaseInitializer.initProducts(6);
    }
}