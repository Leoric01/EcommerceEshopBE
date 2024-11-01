package com.leoric.ecommerceshopbe.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDtoResponse {
    private Long id;

    private String name;
    private String street;
    private String locality;
    private String zip;
    private String city;
    private String country;
    private String mobile;
    private Long order_id;
    private Long seller_id;
    private Long user_id;
}