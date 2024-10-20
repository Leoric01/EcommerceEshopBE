package com.leoric.ecommerceshopbe.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer discount;

    @OneToOne
    private HomeCategory category;

    public Deal(Integer discount, HomeCategory category) {
        this.discount = discount;
        this.category = category;
    }
}