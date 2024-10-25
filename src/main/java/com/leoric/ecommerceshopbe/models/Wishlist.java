package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leoric.ecommerceshopbe.security.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Product> products;
}