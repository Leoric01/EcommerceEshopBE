package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.stripe.PaymentOrder;
import com.leoric.ecommerceshopbe.stripe.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.stripe.StripeConfig;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentOrderStatus;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentLink;
import com.stripe.param.PaymentLinkCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final StripeConfig stripeProperties;
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) {
        if (paymentOrder.getStatus().name().equalsIgnoreCase(PaymentOrderStatus.PENDING.name())) {
            try {
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentId);
                String status = paymentIntent.getStatus();

                if (status.equalsIgnoreCase("succeeded")) {
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
    public String createStripePaymentLink(User user, Long amount, Long orderId) {

        try {
            PaymentLinkCreateParams createParams = PaymentLinkCreateParams.builder()
                    .addLineItem(PaymentLinkCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPrice("price_your_price_id") // Replace with your actual Stripe price ID
                            .build())
                    .setAfterCompletion(
                            PaymentLinkCreateParams.AfterCompletion.builder()
                                    .setRedirect(
                                            PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                    .setUrl(stripeProperties.getEndpointOnSuccess())
                                                    .build())
                                    .build())
                    .setCancelUrl(stripeProperties.getEndpointOnCancel()) // Set the cancel URL here
                    .build();

            // Create the payment link
            PaymentLink paymentLink = PaymentLink.create(createParams);
            return paymentLink.getUrl();
        } catch (StripeException e) {
            log.error("Failed to create Stripe payment link for User {}: {}", user.getId(), e.getMessage(), e);
            return null; // Handle error
        }
    }

    @Override
    public PaymentOrder createPaymentOrder(Authentication authentication, Set<Order> orders) {
        User user = getPrincipalAsUser(authentication);
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) {
        return paymentOrderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Payment order was not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment order by payment link ID was not found"));
    }
}

