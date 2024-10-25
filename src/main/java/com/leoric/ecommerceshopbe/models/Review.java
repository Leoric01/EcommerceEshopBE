package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leoric.ecommerceshopbe.security.auth.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String reviewText;
    @Column(nullable = false)
    private double rating;
    @ElementCollection
    private List<String> productImages;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Product product;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}