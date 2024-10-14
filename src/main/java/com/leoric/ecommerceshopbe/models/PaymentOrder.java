package com.leoric.ecommerceshopbe.models;

import com.leoric.ecommerceshopbe.models.constants.PaymentMethod;
import com.leoric.ecommerceshopbe.models.constants.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;
    private PaymentMethod paymentMethod;
    private String paymentLinkId;

    @ManyToOne
    private User user;
    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
