package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
