package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leoric.ecommerceshopbe.security.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    private Seller seller;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    private LocalDateTime date = LocalDateTime.now();
}