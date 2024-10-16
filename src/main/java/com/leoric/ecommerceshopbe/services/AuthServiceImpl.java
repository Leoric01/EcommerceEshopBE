package com.leoric.ecommerceshopbe.services;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.CartRepository;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.requests.LoginDTOreq;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
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
    private final CartRepository cartRepository;

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
        user.setEnabled(true);
        user = userRepository.save(user);
//        sendValidationEmail(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public AuthenticationResponse signIn(LoginDTOreq req) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        User user = (User) auth.getPrincipal();
        claims.put("fullname", user.getFullName());
        claims.put("email", user.getEmail());
        var jwtToken = jwtProvider.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }

}
