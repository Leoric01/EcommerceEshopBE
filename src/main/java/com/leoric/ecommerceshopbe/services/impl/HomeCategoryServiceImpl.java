package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.repositories.HomeCategoryRepository;
import com.leoric.ecommerceshopbe.services.interfaces.HomeCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homecategoryRepository;

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homecategoryRepository.findAll();
    }

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homecategoryRepository.save(homeCategory);
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long id) {
        HomeCategory existingCategory = homecategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("HomeCategory was not found"));
        if (homeCategory.getImage() != null && !homeCategory.getImage().isBlank()) {
            existingCategory.setImage(homeCategory.getImage());
        }
        if (homeCategory.getCategoryId() != null && !homeCategory.getCategoryId().isBlank()) {
            existingCategory.setCategoryId(homeCategory.getCategoryId());
        }
        return homecategoryRepository.save(existingCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> categories) {
        if (homecategoryRepository.findAll().isEmpty()) {
            return homecategoryRepository.saveAll(categories);
        }
        return homecategoryRepository.findAll();
    }

    @Override
    public void deleteHomeCategoryById(Long id) {

    }
}