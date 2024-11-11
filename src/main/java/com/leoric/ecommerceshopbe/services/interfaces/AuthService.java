package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.auth.dto.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.auth.dto.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.security.auth.dto.SignInRequest;
import com.leoric.ecommerceshopbe.security.auth.dto.SignupRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthenticationResponse signIn(SignInRequest req);

    void signOut(Authentication authentication) throws Exception;

    AccountDetailDto setupPwFromOtp(SetupPwFromOtpReq req);

    void signupAndSendOtp(SignupRequest request);
}