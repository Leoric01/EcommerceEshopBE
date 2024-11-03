package com.leoric.ecommerceshopbe.handler;

public class InvalidAccountTypeAccessException extends RuntimeException {
    public InvalidAccountTypeAccessException(String message) {
        super(message);
    }
}