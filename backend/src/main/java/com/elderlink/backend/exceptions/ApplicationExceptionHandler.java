package com.elderlink.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {


    /**
     * Handles MethodArgumentNotValidException for invalid request arguments.
     * @param e The MethodArgumentNotValidException instance.
     * @return A map containing field names and corresponding error messages.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleInvalidArgument(MethodArgumentNotValidException e){
        Map<String,String> error = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err -> {
            error.put(err.getField(), err.getDefaultMessage());
        });
        return error;
    }

}

