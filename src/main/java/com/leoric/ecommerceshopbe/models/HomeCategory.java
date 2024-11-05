package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private String categoryId;
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
    @Version
    private Integer version;
}