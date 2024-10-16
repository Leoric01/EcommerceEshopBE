package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Category createCategory(Category category);

    List<Category> findAll();

    Category findById(Long id);

    Category save(Category entity);

    void deleteById(Long id);
}
