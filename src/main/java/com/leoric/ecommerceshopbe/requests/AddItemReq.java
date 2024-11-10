package com.leoric.ecommerceshopbe.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddItemReq {
    private String size;
    private int quantity;
    private long productId;
}