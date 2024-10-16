package com.leoric.ecommerceshopbe.response.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Result<T> {
    private boolean success;
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(String message, Integer code) {
        return Result.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> Result<T> success(T data, String message, Integer code) {
        return Result.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Result<T> failure(Integer code, String message, T data) {
        return Result.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return Result.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
}
