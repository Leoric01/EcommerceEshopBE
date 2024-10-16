package com.leoric.ecommerceshopbe.requests;

import lombok.Data;

@Data
public class SetupPwFromOtpReq {
    private String otp;
    private String email;
    private String password;
}
