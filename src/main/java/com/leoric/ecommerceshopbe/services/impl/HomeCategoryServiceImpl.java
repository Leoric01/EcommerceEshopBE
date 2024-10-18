package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.repositories.HomeCategoryRepository;
import com.leoric.ecommerceshopbe.services.interfaces.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepository homecategoryRepository;

    @Override
    public List<HomeCategory> findAll() {
        return homecategoryRepository.findAll();
    }

    @Override
    public HomeCategory findById(Long id) {
        return homecategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("HomeCategory not found"));
    }

    @Override
    public HomeCategory save(HomeCategory entity) {
        return homecategoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        homecategoryRepository.deleteById(id);
    }
}
