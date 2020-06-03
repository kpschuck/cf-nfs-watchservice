package com.example.cfnfswatchservice.error;

import com.example.cfnfswatchservice.error.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //other exception handlers

    @ExceptionHandler(InterruptedException.class)
    protected ResponseEntity<Object> handleEntityNotFound(InterruptedException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        apiError.setMessage(ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}