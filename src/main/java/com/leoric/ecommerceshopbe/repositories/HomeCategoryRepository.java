package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
}
