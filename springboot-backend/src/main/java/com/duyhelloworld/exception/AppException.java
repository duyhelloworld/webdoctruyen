package com.duyhelloworld.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppException extends RuntimeException {
    private String message;
    private HttpStatus statusCode;

    public AppException(HttpStatus statusCode, String message) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
