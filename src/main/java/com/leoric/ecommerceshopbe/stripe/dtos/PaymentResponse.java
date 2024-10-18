package com.leoric.ecommerceshopbe.stripe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String id;
    private int amount;
    private String currency;
    private String status;
}
