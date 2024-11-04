package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leoric.ecommerceshopbe.security.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private double discountPercentage;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private double minimumOrderValue;
    private boolean isActive = true;

    @ManyToMany(mappedBy = "usedCoupons", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> usedByUsers = new HashSet<>();

    public Coupon(String code, double discountPercentage, LocalDate validityStartDate, LocalDate validityEndDate, double minimumOrderValue) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.validityStartDate = validityStartDate;
        this.validityEndDate = validityEndDate;
        this.minimumOrderValue = minimumOrderValue;
    }
}