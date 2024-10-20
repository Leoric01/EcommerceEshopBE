package com.leoric.ecommerceshopbe.security.auth.dto;

import lombok.Data;

@Data
public class SetupPwFromOtpReq {
    private String otp;
    private String email;
    private String password;
}
