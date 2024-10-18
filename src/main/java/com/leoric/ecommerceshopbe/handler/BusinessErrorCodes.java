package com.leoric.ecommerceshopbe.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    EMAIL_ALREADY_IN_USE(305, BAD_REQUEST, "Email is already in use"),
    INVALID_INPUT(400, BAD_REQUEST, "Invalid input provided"),
    ENTITY_NOT_FOUND(404, NOT_FOUND, "Entity was not found"),
    SELLER_RELATED_PROBLEM(601, I_AM_A_TEAPOT, "Seller related problem"),
    ;
    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
