package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
