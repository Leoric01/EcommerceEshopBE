package com.leoric.ecommerceshopbe.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetupPwFromOtpReq {
    @NotBlank(message = "otp field is mandatory")
    private String otp;
    @NotBlank(message = "Email field is mandatory")
    private String email;
    @NotBlank(message = "Password field is mandatory")
    private String password;
}