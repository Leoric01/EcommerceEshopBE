package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.repositories.HomeCategoryRepository;
import com.leoric.ecommerceshopbe.services.interfaces.HomeCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
    @Transactional
    public synchronized List<HomeCategory> createCategories(List<HomeCategory> categories) {
        List<HomeCategory> existingCategories = homecategoryRepository.findAll();
        log.warn("HOMECATEGORYSERVICE: Create Categories for {} Categories inside{}", categories.size(), UUID.randomUUID());

        if (existingCategories.size() < 2) {
            try {
                List<HomeCategory> newCategories = categories.stream()
                        .filter(category -> existingCategories.stream().noneMatch(existingCategory -> existingCategory.getName().equals(category.getName())))
                        .collect(Collectors.toList());

                return homecategoryRepository.saveAll(newCategories);
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException(e.getMessage());
            }
        }
        return existingCategories;
    }

    @Override
    public void deleteHomeCategoryById(Long id) {
        homecategoryRepository.deleteById(id);
    }
}