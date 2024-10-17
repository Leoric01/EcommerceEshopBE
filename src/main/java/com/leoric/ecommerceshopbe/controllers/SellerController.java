package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.requests.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.requests.SignInRequest;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.requests.VerificationCodeReq;
import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/")
public class SellerController {

    private final SellerService sellerService;
    private final AuthService authService;

    @GetMapping()
    public List<Seller> getAllSellers() {
        return sellerService.findAll();
    }

    @PostMapping("/signin")
    public ResponseEntity<Result<AuthenticationResponse>> loginSeller(@RequestBody SignInRequest req) {
        AuthenticationResponse authResponse = authService.signIn(req);
        Result<AuthenticationResponse> response = Result.success(authResponse, "Login successful", OK.value());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    @ResponseStatus(CREATED)
    public ResponseEntity<Result<String>> createUserHandler(@RequestBody @Valid SignupRequest req) {
        authService.signup(req);

        Result<String> response = Result.success("Seller registered successfully!", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<Result<VerificationCodeReq>> sentOtpHandler(@RequestBody @Valid VerificationCodeReq req) {
        authService.sentLoginOtp(req);
        // TODO CHANGE IT, THIS IS FOR DEVELOPMENT ONLY
//        String code = userService.findByEmail(req.getEmail().substring(8)).getVerificationCode().getOtp();
        Result<VerificationCodeReq> response = Result.success(req, "Verification code otp was sent value: \n ", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/set-pw")
    public ResponseEntity<Result<AccountDetailDto>> setUpPassword(
            @RequestBody @Valid SetupPwFromOtpReq req) throws BadRequestException {
        AccountDetailDto accountDetailDto = authService.setupPwFromOtp(req);
        Result<AccountDetailDto> response = Result.success(accountDetailDto, "Password set up successfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }
}
