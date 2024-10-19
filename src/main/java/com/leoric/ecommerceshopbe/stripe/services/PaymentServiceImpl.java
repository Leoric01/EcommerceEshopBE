package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.stripe.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.stripe.StripeConfig;
import com.leoric.ecommerceshopbe.stripe.model.PaymentOrder;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentOrderStatus;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
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
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeProperties.getEndpointOnSuccess() + "/" + orderId)
                .setCancelUrl(stripeProperties.getEndpointOnCancel())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("eur")
                                .setUnitAmount(amount * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem
                                                .PriceData
                                                .ProductData
                                                .builder()
                                                .setName("George's first Eshop")
                                                .build()
                                ).build()
                        ).build()
                ).build();
        Session session = Session.create(params);
        return session.getUrl();

    }

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

