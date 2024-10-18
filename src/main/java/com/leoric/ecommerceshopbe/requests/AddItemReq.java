package com.leoric.ecommerceshopbe.requests;

import lombok.Data;

@Data
public class AddItemReq {
    private String size;
    private int quantity;
    private long productId;
}
