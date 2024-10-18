package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long id);

    @Query("""
            SELECT p FROM Product p
            WHERE (:query is null OR lower(p.title) LIKE lower(concat('%', :query, '%')))
            OR (:query is null OR lower(p.category.name) LIKE lower(concat('%', :query, '%')))
            """)
    List<Product> searchProducts(@Param("query") String query);
}
