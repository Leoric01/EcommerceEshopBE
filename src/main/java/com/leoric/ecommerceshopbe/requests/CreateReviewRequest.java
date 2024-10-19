package com.leoric.ecommerceshopbe.requests;

import lombok.Data;

import java.util.List;

@Data
public class CreateReviewRequest {
    private String reviewText;
    private double rating;
    private List<String> images;
}
