package com.leoric.ecommerceshopbe.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @JoinColumn(name = "seller_id")
    @OneToOne
    private Seller seller;

    private Long totalEarnings = 0L;
    private Long totalSales = 0L;
    private Long totalRefunds = 0L;
    private Long totalTax = 0L;
    private Long netEarnings = 0L;
    private Integer totalOrders = 0;
    private Integer canceledOrders = 0;
    private Integer totalTransactions = 0;


}
