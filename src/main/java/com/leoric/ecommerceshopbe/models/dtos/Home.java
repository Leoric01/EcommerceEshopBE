package com.leoric.ecommerceshopbe.models.dtos;

import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.models.HomeCategory;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Home {
    private List<HomeCategory> grid;
    private List<HomeCategory> shopByCategories;
    private List<HomeCategory> electricCategories;
    private List<HomeCategory> dealCategories;
    private List<Deal> deals;
}