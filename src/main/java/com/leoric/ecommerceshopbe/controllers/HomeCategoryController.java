package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.models.dtos.Home;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.HomeCategoryService;
import com.leoric.ecommerceshopbe.services.interfaces.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/home")
public class HomeCategoryController {
    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;

    @PostMapping("/categories")
    public ResponseEntity<Result<Home>> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        Result<Home> response = Result.success(home, "home created", OK.value());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/categories")
    public ResponseEntity<Result<List<HomeCategory>>> getHomeCategory() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        Result<List<HomeCategory>> response = Result.success(categories, "categories retrieved", OK.value());
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/category/{id}")
    public ResponseEntity<Result<HomeCategory>> updateHomeCategory(@PathVariable Long id,
                                                                   @RequestBody HomeCategory homeCategory) {
        HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory,id);
        Result<HomeCategory> response = Result.success(updatedCategory, "category updated", OK.value());
        return ResponseEntity.ok(response);
    }
}