package com.leoric.ecommerceshopbe.security.auth;

import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.security.auth.dto.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.auth.dto.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.security.auth.dto.SignInRequest;
import com.leoric.ecommerceshopbe.security.auth.dto.SignupRequest;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signout")
    public ResponseEntity<Result<Void>> signOut(Authentication connectedUser) throws Exception {
        authService.signOut(connectedUser);
        return ResponseEntity.ok(Result.success("Successfully signed out", OK.value()));
    }

    @PostMapping("/signup")
    @ResponseStatus(CREATED)
    public ResponseEntity<Result<String>> createUserHandler(@RequestBody @Valid SignupRequest req) {
        authService.signupAndSendOtp(req);
        Result<String> response = Result.success("User registered successfully!", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<Result<AuthenticationResponse>> login(@RequestBody @Valid SignInRequest req) {
        AuthenticationResponse authResponse = authService.signIn(req);

        Result<AuthenticationResponse> response = Result.success(authResponse, "Login successful", OK.value());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/set-pw")
    public ResponseEntity<Result<AccountDetailDto>> setUpPassword(
            @RequestBody @Valid SetupPwFromOtpReq req) throws BadRequestException {
        AccountDetailDto accountDetailDto = authService.setupPwFromOtp(req);
        Result<AccountDetailDto> response = Result.success(accountDetailDto, "Password set up successfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<Result<List<AccountDetailDto>>> allUsers() {
        List<AccountDetailDto> users = userService.findAllUsersToDto();

        Result<List<AccountDetailDto>> response = Result.success(users, "User list fetched successfully", OK.value());
        return ResponseEntity.ok().body(response);
    }
}