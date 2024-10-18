package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.stripe.PaymentOrder;
import com.leoric.ecommerceshopbe.stripe.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.stripe.StripeConfig;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentOrderStatus;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentStatus;
import com.stripe.Stripe;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.leoric.ecommerceshopbe.stripe.constants.PaymentOrderStatus.PENDING;
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final StripeConfig stripeProperties;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentOrderRepository payementOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        if (paymentOrder.getStatus().name().equalsIgnoreCase(PENDING.name())) {
            // proceed with payment
            Stripe.get;
            Object payment = Stripe.get...fetch();
            String status = payment.get("status");
            if (status.equalsIgnoreCase("capture")) {
                Set<Order> orders = paymentOrder.getOrders();
                for (Order order : orders) {
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS) {
                }
            }
        }

        return false;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) {
        return "";
    }

    @Override
    public PaymentOrder createPaymentOrder(Authentication authentication, Set<Order> orders) {
        User user = getPrincipalAsUser(authentication);
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) {
        return payementOrderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("payment was not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentId).orElseThrow(() -> new EntityNotFoundException("Payment findByPaymentLinkId(String paymentId) was not found"));

    }
}
