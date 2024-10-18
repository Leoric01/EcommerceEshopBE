package com.leoric.ecommerceshopbe.requests;

import lombok.Data;

@Data
public class CartItemQuantityUpdateReq {
    private long cartItemId;
    private int quantity;
}
