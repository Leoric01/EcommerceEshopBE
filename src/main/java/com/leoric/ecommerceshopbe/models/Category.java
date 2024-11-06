package com.leoric.ecommerceshopbe.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"category_id", "level"})})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    @NotNull
    @Column(name = "category_id")
    @EqualsAndHashCode.Include
    private String categoryId;

    @ManyToOne
    private Category parentCategory;

    @NotNull
    @EqualsAndHashCode.Include
    private Integer level;
}