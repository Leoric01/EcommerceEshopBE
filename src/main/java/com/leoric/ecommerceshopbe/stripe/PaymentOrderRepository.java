package com.leoric.ecommerceshopbe.stripe;

import com.leoric.ecommerceshopbe.stripe.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
    Optional<PaymentOrder> findByPaymentLinkId(String paymentLinkId);
}