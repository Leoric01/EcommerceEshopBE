package com.leoric.ecommerceshopbe.models;

import com.leoric.ecommerceshopbe.models.constants.PaymentMethod;
import com.leoric.ecommerceshopbe.models.constants.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static com.leoric.ecommerceshopbe.models.constants.PaymentOrderStatus.PENDING;

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

    @ManyToOne
    private User user;
    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
