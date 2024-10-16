package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.requests.LoginDTOreq;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthenticationResponse signIn(LoginDTOreq req);

    void register(SignupRequest request) throws EmailAlreadyInUseException;
}
