package com.leoric.ecommerceshopbe.stripe.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkRequest {
    private int amount;
    private String currency;
    private String token;
}
