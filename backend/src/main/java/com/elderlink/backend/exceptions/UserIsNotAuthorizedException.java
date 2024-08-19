package com.elderlink.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserIsNotAuthorizedException extends RuntimeException{

    public UserIsNotAuthorizedException(String message){
        super(message);
    }

}