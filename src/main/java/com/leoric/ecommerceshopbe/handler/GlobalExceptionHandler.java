package com.leoric.ecommerceshopbe.handler;

import com.leoric.ecommerceshopbe.response.common.Result;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.leoric.ecommerceshopbe.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpVerificationException.class)
    public ResponseEntity<Result<Void>> handleOtpVerificationException(OtpVerificationException exp) {
        log.warn("OTP verification failed: {}", exp.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(Result.failure(BAD_REQUEST.value(), exp.getMessage()));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Result<Void>> handleLockedException(LockedException exp) {
        log.warn("Account locked: {}", exp.getMessage());
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(Result.failure(ACCOUNT_LOCKED.getCode(), ACCOUNT_LOCKED.getDescription()));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<Result<Void>> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        log.warn("Email already in use: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(Result.failure(CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Result<Void>> handleDisabledException(DisabledException exp) {
        log.warn("Account disabled: {}", exp.getMessage());
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(Result.failure(ACCOUNT_DISABLED.getCode(), ACCOUNT_DISABLED.getDescription()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result<Void>> handleBadCredentialsException(BadCredentialsException exp) {
        log.warn("Bad credentials: {}", exp.getMessage());
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(Result.failure(BAD_CREDENTIALS.getCode(), BAD_CREDENTIALS.getDescription()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Result<Void>> handleMessagingException(MessagingException exp) {
        log.error("Messaging error: {}", exp.getMessage());
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(Result.failure(INTERNAL_SERVER_ERROR.value(), exp.getMessage()));
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<Result<Void>> handleOperationNotPermittedException(OperationNotPermittedException exp) {
        log.warn("Operation not permitted: {}", exp.getMessage());
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(Result.failure(BAD_REQUEST.value(), exp.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Set<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        log.warn("Validation errors: {}", errors);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(Result.failure(BAD_REQUEST.value(), "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleGenericException(Exception exp) {
        // Log the exception with error level
        log.error("Unexpected error occurred: {}", exp.getMessage(), exp);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(Result.failure(INTERNAL_SERVER_ERROR.value(), "Internal error, please contact the admin"));
    }
}
