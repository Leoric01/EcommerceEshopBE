package com.leoric.ecommerceshopbe.stripe.model;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentMethod;
import com.leoric.ecommerceshopbe.stripe.model.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.leoric.ecommerceshopbe.stripe.model.enums.PaymentOrderStatus.PENDING;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    @Enumerated(EnumType.STRING)
    private PaymentOrderStatus status = PENDING;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentLinkId;
    private String stripeSessionId;
    @ManyToOne
    private User user;
    @OneToMany
    private Set<Order> orders = new HashSet<>();
}