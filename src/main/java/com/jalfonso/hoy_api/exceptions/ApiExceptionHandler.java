package com.jalfonso.hoy_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jalfonso.hoy_api.model.ErrorResponse;

@RestControllerAdvice
public class ApiExceptionHandler extends RuntimeException {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(BadRequestException ex){
        ErrorResponse errorResponse = ErrorResponse.builder().code(ex.getCode()).error(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InternalServerExcepcion.class)
    public ResponseEntity<ErrorResponse> handleInternalServerExceptions(InternalServerExcepcion ex){
        ErrorResponse errorResponse = ErrorResponse.builder().code(ex.getCode()).error(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex){
        ErrorResponse errorResponse = ErrorResponse.builder().code(ex.getCode()).error(ex.getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}