package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.stripe.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.stripe.StripeConfig;
import com.leoric.ecommerceshopbe.stripe.model.PaymentOrder;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentOrderStatus;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentStatus;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final StripeConfig stripeProperties;
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private final GlobalUtil globalUtil;

    @Override
    @Transactional
    public PaymentOrder createPaymentOrder(Authentication authentication, Set<Order> orders) {
        User user = globalUtil.getPrincipalAsUser(authentication);
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeProperties.getEndpointOnSuccess() + orderId)
                .setCancelUrl(stripeProperties.getEndpointOnCancel())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();
        Session session = Session.create(params);
        PaymentOrder paymentOrder = paymentOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("PaymentOrder not found"));
        paymentOrder.setPaymentLinkId(session.getPaymentLink());
        paymentOrder.setStripeSessionId(session.getId());
        paymentOrderRepository.save(paymentOrder);
        return session.getUrl();
    }

    // TODO FIX THIS - SESSION NULL, ID IS WRONG
    @Override
    @Transactional
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        if (paymentOrder.getStatus().name().equalsIgnoreCase(PaymentOrderStatus.PENDING.name())) {
            try {
                String stripeSessionId = paymentOrder.getStripeSessionId();
                Session session = Session.retrieve(stripeSessionId);
                System.out.println("SESSION _____ " + session);
                String status = session.getStatus();

                if (status.equalsIgnoreCase("complete")) {
                    Set<Order> orders = paymentOrder.getOrders();
                    for (Order order : orders) {
                        order.setPaymentStatus(PaymentStatus.COMPLETED);
                        orderRepository.save(order);
                    }
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                } else {
                    // Payment failed
                    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                    paymentOrderRepository.save(paymentOrder);
                    return false;
                }
            } catch (StripeException e) {
                log.error("StripeException occurred while processing payment for PaymentOrder {}: {}", paymentOrder.getId(), e.getMessage(), e);
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public PaymentOrder getPaymentOrderById(String orderId) {
        long longOrderId;
        try {
            longOrderId = Long.parseLong(orderId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The provided order Id is not a valid number");
        }
        return paymentOrderRepository.findById(longOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Payment order was not found"));
    }

    @Override
    @Transactional
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentLinkId)
                .orElseThrow(() -> new EntityNotFoundException("Payment order by payment link ID was not found"));
    }
}