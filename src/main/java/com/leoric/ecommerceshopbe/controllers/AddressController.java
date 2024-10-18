package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<Set<Address>> getAll(
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(addressService.findAllUsersAddresses(connectedUser));
    }

}
