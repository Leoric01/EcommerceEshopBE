package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);

    Optional<Category> findByCategoryIdAndLevel(String categoryId, Integer level);

}