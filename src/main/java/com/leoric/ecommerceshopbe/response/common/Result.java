package com.leoric.ecommerceshopbe.response.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {
    private boolean success;  // true for successful operations, false for failures
    private Integer code;     // HTTP status code
    private String message;   // Message to provide more info about the result
    private T data;           // Data related to the response (if any)

    public Result(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Success methods
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, 200, message, data);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(true, 200, message, null);
    }

    // Failure methods
    public static <T> Result<T> failure(Integer code, String message, T data) {
        return new Result<>(false, code, message, data);
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<>(false, code, message, null);
    }
}
