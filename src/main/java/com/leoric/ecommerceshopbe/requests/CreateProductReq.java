package com.leoric.ecommerceshopbe.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateProductReq {
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String color;
    private List<String> images;
    private String category;
    private String category2;
    private String category3;
    private String sizes;
}
