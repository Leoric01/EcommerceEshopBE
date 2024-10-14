package com.leoric.ecommerceshopbe.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL)
    private VerificationCode verificationCode;

    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL)
    private SellerReport sellerReport;
}
