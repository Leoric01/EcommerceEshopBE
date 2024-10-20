package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.auth.dto.*;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthenticationResponse signIn(SignInRequest req);

    void signup(SignupRequest request);

    void signOut(Authentication authentication) throws Exception;

    void sentLoginOtp(VerificationCodeReq email);

    AccountDetailDto setupPwFromOtp(SetupPwFromOtpReq req) throws BadRequestException;
}