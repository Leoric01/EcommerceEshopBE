package com.leoric.ecommerceshopbe.models;

import com.leoric.ecommerceshopbe.models.constants.HomeCategorySection;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HomeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    private String categoryId;
    @Enumerated(EnumType.STRING)
    private HomeCategorySection section;
}
