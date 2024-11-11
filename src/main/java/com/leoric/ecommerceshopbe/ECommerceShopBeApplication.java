package com.leoric.ecommerceshopbe;

import com.leoric.ecommerceshopbe.utils.datainit.DatabaseInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
public class ECommerceShopBeApplication {

    private final DatabaseInitializer databaseInitializer;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ECommerceShopBeApplication.class, args);
//        GlobalUtil.duplicatedFieldsFinder("pathfromroot", "womenlevelthree");
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return this::run;
    }

    private void run(String... args) {
        databaseInitializer.initSellers(6);
        databaseInitializer.initCategories();
        databaseInitializer.initUsers(5, 5);
        databaseInitializer.initAdmins(2);
        databaseInitializer.initCreateCoupons();
        databaseInitializer.initHomeCategories();
        databaseInitializer.initDeals();
        databaseInitializer.initProducts();
    }
}