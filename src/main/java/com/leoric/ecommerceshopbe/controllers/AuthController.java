package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.requests.SignInRequest;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signout")
    public ResponseEntity<Result<Void>> signOut(Authentication connectedUser) {
        authService.signOut(connectedUser);
        return ResponseEntity.ok(Result.success("Successfully signed out", OK.value()));
    }

    @PostMapping("/signup")
    @ResponseStatus(CREATED)
    public ResponseEntity<Result<String>> createUserHandler(@RequestBody @Valid SignupRequest req) {
        authService.signup(req);

        Result<String> response = Result.success("User registered successfully!", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<Result<AuthenticationResponse>> login(@RequestBody @Valid SignInRequest req) {
        AuthenticationResponse authResponse = authService.signIn(req);

        Result<AuthenticationResponse> response = Result.success(authResponse, "Login successful", OK.value());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping()
    public ResponseEntity<Result<List<User>>> allUsers() {
        List<User> users = userRepository.findAll();

        Result<List<User>> response = Result.success(users, "User list fetched successfully", OK.value());
        return ResponseEntity.ok().body(response);
    }
}
