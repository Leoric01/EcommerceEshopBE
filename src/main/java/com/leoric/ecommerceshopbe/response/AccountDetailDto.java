package com.leoric.ecommerceshopbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto {
    private long id;
    private String name;
    private String email;
    private String mobile;
    private String role;
}
