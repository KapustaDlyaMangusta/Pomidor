package com.chervonyi.pomidor.presentation.handlers;

import com.chervonyi.pomidor.domain.shared.ApplicationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationError> handleException(Exception ex) {
        var errorResponse = new ApplicationError(
                "Error.InternalServerError",
                ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}