package com.leoric.ecommerceshopbe.response;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.OrderItem;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.stripe.model.dtos.PaymentDetails;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoResponse {
    private Long id;
    private String orderId;
    private User user;
    private Long sellerId;
    private List<OrderItem> orderItems = new ArrayList<>();
    private Address shippingAddress;
    private PaymentDetails paymentDetails = new PaymentDetails();
    private double totalMaxPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    private OrderStatus orderStatus;
    private int totalItem;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    private LocalDateTime deliverDate;
}