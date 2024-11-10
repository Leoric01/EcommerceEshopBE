package com.leoric.ecommerceshopbe.requests.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateReqDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}