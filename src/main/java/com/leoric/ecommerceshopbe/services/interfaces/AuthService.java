package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.requests.SignInRequest;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthenticationResponse signIn(SignInRequest req);

    void signup(SignupRequest request);

    void signOut(Authentication authentication);

    void sentLoginOtp(String email);


}
