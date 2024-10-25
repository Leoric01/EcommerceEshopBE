package com.leoric.ecommerceshopbe.requests.dto;

import lombok.Data;

@Data
public class UserUpdateReqDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}