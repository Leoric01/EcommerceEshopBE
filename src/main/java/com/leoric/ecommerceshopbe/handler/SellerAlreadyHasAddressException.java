package com.leoric.ecommerceshopbe.handler;

import com.leoric.ecommerceshopbe.models.Address;
import lombok.Getter;

@Getter
public class SellerAlreadyHasAddressException extends RuntimeException {
    private final Address existingAddress;

    public SellerAlreadyHasAddressException(String msg, Address existingAddress) {
        super(msg);
        this.existingAddress = existingAddress;
    }

}