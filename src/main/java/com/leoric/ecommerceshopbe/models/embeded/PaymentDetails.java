package com.leoric.ecommerceshopbe.models.embeded;

import com.leoric.ecommerceshopbe.models.constants.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDetails {
    private String paymentId;
    private String paymentLinkId;
    private String paymentLinkReferenceId;
    private String paymentLinkStatus;
    private String paymentIdZWSP;
    private PaymentStatus status;
}
