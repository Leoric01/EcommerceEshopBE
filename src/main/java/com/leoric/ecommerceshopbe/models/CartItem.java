package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private String size;
    private int quantity = 1;
    private Integer maxPrice;
    private Integer sellingPrice;
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}