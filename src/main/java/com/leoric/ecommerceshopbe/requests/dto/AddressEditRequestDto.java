package com.leoric.ecommerceshopbe.requests.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEditRequestDto {

    @NotBlank(message = "Name cannot be blank when updating the address")
    private String name;

    @NotBlank(message = "Street cannot be blank when updating the address")
    private String street;

    private String locality;

    @NotBlank(message = "ZIP code cannot be blank when updating the address")
    private String zip;

    @NotBlank(message = "City cannot be blank when updating the address")
    private String city;

    @NotBlank(message = "Country cannot be blank when updating the address")
    private String country;

    private String mobile;
}