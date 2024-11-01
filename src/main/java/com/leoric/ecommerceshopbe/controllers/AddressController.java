package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.response.AddressDtoResponse;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsSeller;
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<Set<AddressDtoResponse>> getAll(
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(addressService.findAllUsersAddresses(connectedUser));
    }

    @PostMapping(value = "/user")
    public ResponseEntity<Result<Address>> addUserAddress(Authentication authentication, @Valid @RequestBody AddAddressRequestDTO address) {
        User user = getPrincipalAsUser(authentication);
        Address createdAddress = addressService.addUserAddress(user.getId(), address);
        Result<Address> result = Result.success(createdAddress, "Address created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }

    @PostMapping("/seller")
    public ResponseEntity<Result<Address>> addSellerAddress(Authentication authentication, @RequestBody AddAddressRequestDTO address) {
        Seller seller = getPrincipalAsSeller(authentication);
        Address createdAddress = addressService.addSellerAddress(seller.getId(), address);
        Result<Address> result = Result.success(createdAddress, "Address created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }
}