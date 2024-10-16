package com.leoric.ecommerceshopbe.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignupRequest {
    @NotEmpty(message = "Email field is mandatory")
    @NotBlank(message = "Email field is mandatory")
    @Email(message = "Email is not in valid format")
    private String email;
    private String otp;
    @NotEmpty(message = "First name field is mandatory")
    @NotBlank(message = "First name field is mandatory")
    private String firstName;
    @NotEmpty(message = "Last name field is mandatory")
    @NotBlank(message = "Last name field is mandatory")
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
//    @NotEmpty(message = "Password name is mandatory")
//    @NotBlank(message = "Password name is mandatory")
//    private String password;
}
