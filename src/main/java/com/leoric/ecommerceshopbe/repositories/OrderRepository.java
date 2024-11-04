package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.product", "orderItems.product.seller"})
    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.product", "orderItems.product.seller", "shippingAddress"})
    List<Order> findAllBySellerId(Long sellerId);
}