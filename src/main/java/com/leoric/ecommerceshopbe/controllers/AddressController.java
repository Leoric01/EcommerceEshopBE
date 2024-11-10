package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.response.AddressDtoResponse;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final GlobalUtil globalUtil;

    @PostMapping("/user")
    public ResponseEntity<Result<Address>> addUserAddress(Authentication authentication, @Valid @RequestBody AddAddressRequestDTO address) {
        User user = globalUtil.getPrincipalAsUser(authentication);
        Address createdAddress = addressService.addUserAddress(user.getId(), address);
        Result<Address> result = Result.success(createdAddress, "Address created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }

    @PatchMapping("/user/{addressId}")
    public ResponseEntity<Result<Address>> editUserAddress(Authentication authentication, @RequestBody AddAddressRequestDTO address, @PathVariable Long addressId) {
        User user = globalUtil.getPrincipalAsUser(authentication);
        Address editedAddress = addressService.editUserAddress(user.getId(), address, addressId);
        Result<Address> result = Result.success(editedAddress, "Address created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<Set<AddressDtoResponse>> getAll(
            Authentication connectedAccount
    ) {
        Account account = globalUtil.getAccountFromAuthentication(connectedAccount);
        if (account instanceof User) {
            return ResponseEntity.ok(addressService.findAllUsersAddresses(connectedAccount));
        } else if (account instanceof Seller) {
            return ResponseEntity.ok(addressService.findSellerAddress(connectedAccount));
        } else {
            throw new IllegalStateException("Unknown account type");
        }
    }

    @PostMapping("/seller")
    public ResponseEntity<Result<Address>> addSellerAddress(Authentication authentication, @RequestBody AddAddressRequestDTO address) {
        Seller seller = globalUtil.getPrincipalAsSeller(authentication);
        Address createdAddress = addressService.addSellerAddress(seller.getId(), address);
        Result<Address> result = Result.success(createdAddress, "Address created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }

    @PatchMapping("/seller")
    public ResponseEntity<Result<Address>> editSellerAddress(Authentication authentication, @RequestBody AddAddressRequestDTO address) {
        Address editedAddress = addressService.editSellerAddress(authentication, address);
        Result<Address> result = Result.success(editedAddress, "Sellers address edit success", CREATED.value());
        return ResponseEntity.status(CREATED).body(result);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Result<Void>> deleteAddress(Authentication authentication, @PathVariable Long addressId) {
        addressService.deleteAddress(authentication, addressId);
        Result<Void> result = Result.success("Address deleted success", OK.value());
        return ResponseEntity.status(OK).body(result);
    }
}