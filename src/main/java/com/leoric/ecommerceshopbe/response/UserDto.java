package com.leoric.ecommerceshopbe.response;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String name;
    private String email;
    private String mobile;
    private String role;
}
