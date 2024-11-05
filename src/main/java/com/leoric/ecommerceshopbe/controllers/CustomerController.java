package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.models.dtos.Home;
import com.leoric.ecommerceshopbe.services.interfaces.HomeCategoryService;
import com.leoric.ecommerceshopbe.services.interfaces.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @GetMapping("/home-page")
    public ResponseEntity<Home> getHomePageData() {
        Home homePageData = homeService.getHomePageData();
        return ResponseEntity.accepted().body(homePageData);
    }

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        log.warn("CUSTOMER CONTROLLER : Create Categories for {} Categories inside{}", homeCategories.size(), UUID.randomUUID());
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return ResponseEntity.accepted().body(home);
    }
}