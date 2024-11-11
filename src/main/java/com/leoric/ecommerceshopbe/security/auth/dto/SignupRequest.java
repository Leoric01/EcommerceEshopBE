package com.leoric.ecommerceshopbe.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignupRequest {
    @NotEmpty(message = "Email field is mandatory")
    @NotBlank(message = "Email field is mandatory")
    @Email(message = "Email is not in valid format")
    private String email;
    @NotEmpty(message = "First name field is mandatory")
    @NotBlank(message = "First name field is mandatory")
    private String firstName;
    @NotEmpty(message = "Last name field is mandatory")
    @NotBlank(message = "Last name field is mandatory")
    private String lastName;
    @NotBlank(message = "account type field is mandatory")
    @NotEmpty(message = "account type field is mandatory")
    private String account;
    public String getFullName() {
        return firstName + " " + lastName;
    }
}