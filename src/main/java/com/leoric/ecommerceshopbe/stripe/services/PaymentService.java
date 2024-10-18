package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.stripe.PaymentOrder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface PaymentService {
    PaymentOrder createPaymentOrder(Authentication authentication, Set<Order> orders);

    PaymentOrder getPaymentOrderById(Long orderId);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId);

    String createStripePaymentLink(User user, Long amount, Long orderId);
}
