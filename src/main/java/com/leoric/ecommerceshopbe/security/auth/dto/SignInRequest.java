package com.leoric.ecommerceshopbe.security.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty(message = "Email name is mandatory")
    @NotBlank(message = "Email name is mandatory")
    @Email(message = "Email is not in valid format")
    private String email;
    @NotEmpty(message = "Password name is mandatory")
    @NotBlank(message = "Password name is mandatory")
    private String password;
}
