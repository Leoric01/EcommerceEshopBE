package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}
