package com.leoric.ecommerceshopbe.stripe.dtos;

import com.leoric.ecommerceshopbe.stripe.constants.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class PaymentDetails {
    private String paymentId;
    private String paymentLinkId;
    private String paymentLinkReferenceId;
    private String paymentLinkStatus;
    private String paymentIdZWSP;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
