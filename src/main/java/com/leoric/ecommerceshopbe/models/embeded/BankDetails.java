package com.leoric.ecommerceshopbe.models.embeded;

import lombok.Data;

@Data
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
