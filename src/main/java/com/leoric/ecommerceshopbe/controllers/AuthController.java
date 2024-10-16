package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.requests.LoginDTOreq;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Result<String>> createUserHandler(@RequestBody @Valid SignupRequest req) {
        authService.register(req);

        Result<String> response = Result.success("User registered successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<Result<AuthenticationResponse>> login(@RequestBody LoginDTOreq req) {
        AuthenticationResponse authResponse = authService.signIn(req);

        Result<AuthenticationResponse> response = Result.success(authResponse, "Login successful");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping()
    public ResponseEntity<Result<List<User>>> allUsers() {
        List<User> users = userRepository.findAll();

        Result<List<User>> response = Result.success(users, "User list fetched successfully");
        return ResponseEntity.ok().body(response);
    }
}
