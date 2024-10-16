package com.leoric.ecommerceshopbe.response;

import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
    private USER_ROLE role;
}
