package com.leoric.ecommerceshopbe.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemQuantityUpdateReq {
    private long cartItemId;
    private int quantity;
}