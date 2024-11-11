package com.leoric.ecommerceshopbe.requests.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductReqDto {
    private String title;
    private String description;
    private Integer maxPrice;
    private Integer sellingPrice;
    private String color;
    private List<String> images;
    private String category;
    private String category2;
    private String category3;
    private String sizes;
    private int quantity;

    @Override
    public String toString() {
        return "CreateProductReqDto{" +
               "title='" + title + '\'' +
               ", description='" + description + '\'' +
               ", maxPrice=" + maxPrice +
               ", sellingPrice=" + sellingPrice +
               ", color='" + color + '\'' +
               ", images=" + images +
               ", category='" + category + '\'' +
               ", category2='" + category2 + '\'' +
               ", category3='" + category3 + '\'' +
               ", sizes='" + sizes + '\'' +
               ", quantity=" + quantity +
               '}';
    }
}