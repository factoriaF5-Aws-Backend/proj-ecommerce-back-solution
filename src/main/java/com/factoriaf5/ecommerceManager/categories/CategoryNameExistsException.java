package com.factoriaf5.ecommerceManager.categories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class CategoryNameExistsException extends RuntimeException {
    public CategoryNameExistsException(String message) {
        super(message);
    }
}
