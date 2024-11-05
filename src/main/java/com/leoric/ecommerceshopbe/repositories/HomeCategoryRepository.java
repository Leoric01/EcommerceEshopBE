package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
    boolean existsByName(String name);

    Optional<HomeCategory> findFirstByCategoryId(String categoryId);
}