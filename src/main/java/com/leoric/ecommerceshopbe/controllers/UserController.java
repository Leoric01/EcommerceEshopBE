package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<Result<AccountDetailDto>> getUserProfile(Authentication connectedUser
    ) {
        AccountDetailDto accountDetailDto = userService.currentUser(connectedUser);
        Result<AccountDetailDto> response = Result.success(accountDetailDto, "User's details fetched succesfully", HttpStatus.CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }
}
