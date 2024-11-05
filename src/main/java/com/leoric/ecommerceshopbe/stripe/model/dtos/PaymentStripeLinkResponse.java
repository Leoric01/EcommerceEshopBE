package com.leoric.ecommerceshopbe.stripe.model.dtos;

import lombok.Data;

@Data
public class PaymentStripeLinkResponse {
    private String payment_link_url;
}