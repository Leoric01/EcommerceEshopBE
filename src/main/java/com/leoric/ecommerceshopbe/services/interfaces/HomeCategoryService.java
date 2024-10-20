package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeCategoryService {

    List<HomeCategory> getAllHomeCategories();
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id);
    List<HomeCategory> createCategories(List<HomeCategory> categories);
    void deleteHomeCategoryById(Long id);
}