package com.factoriaf5.ecommerceManager.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyRegisterException extends RuntimeException{
    public UserAlreadyRegisterException(String message) {
        super(message);
    }
}
