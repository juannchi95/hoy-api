package com.jalfonso.hoy_api.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private String code;
    private String message;

    public BadRequestException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}