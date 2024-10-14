package com.leoric.ecommerceshopbe.models;

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

    //    @JoinColumn(name = "user_id")
    @ManyToOne
    private User customer;

    //    @JoinColumn(name = "seller_id")
    @ManyToOne
    private Seller seller;

    //    @JoinColumn(name = "order_id")
    @OneToOne
    private Order order;

    private LocalDateTime date = LocalDateTime.now();
}
