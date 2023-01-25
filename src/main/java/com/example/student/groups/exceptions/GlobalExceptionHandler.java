package com.example.student.groups.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(AlreadyExistsException e) {
        ErrorResponse error = new ErrorResponse();
        error.setError(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}