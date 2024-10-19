package com.leoric.ecommerceshopbe.stripe.model.dtos;

import lombok.Data;

@Data
public class PaymentLinkResponse {
    private String payment_link_url;
    private String payment_link_id;
}
