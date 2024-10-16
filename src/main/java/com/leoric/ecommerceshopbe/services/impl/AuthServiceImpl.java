package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.requests.LoginDTOreq;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void register(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("Email is already in use");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(ROLE_USER)
                .build();
        userRepository.save(user);
//        sendValidationEmail(user);
    }

    @Override
    public AuthenticationResponse signIn(LoginDTOreq req) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtProvider.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
