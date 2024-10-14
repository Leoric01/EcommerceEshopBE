package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

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

    @JsonIgnore
    @ManyToOne
    @NotNull
//    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @NotNull
//    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
