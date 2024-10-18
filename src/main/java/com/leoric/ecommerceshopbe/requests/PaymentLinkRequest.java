package com.leoric.ecommerceshopbe.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkRequest {
    private int amount;
    private String currency;
    private String token;
}
