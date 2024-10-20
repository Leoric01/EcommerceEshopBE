package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leoric.ecommerceshopbe.security.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> cartItems = new HashSet<>();
    private double preDiscountPrice;
    private double totalSellingPrice;
    private double totalItem;
    private double totalMrpPrice;
    private int discount;
    private String couponCode;

}