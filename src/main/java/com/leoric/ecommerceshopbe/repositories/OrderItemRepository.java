package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.OrderItem;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"product.seller"})
    Optional<OrderItem> findById(@NonNull Long id);
}