package com.leoric.ecommerceshopbe.requests.dto;

import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerEditRequestDto {
    private String sellerName;
    private String email;
    private String password;

    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private AddressEditRequestDto pickupAddress;
    private String vat;
}