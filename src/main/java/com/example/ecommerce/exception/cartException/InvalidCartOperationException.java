package com.example.ecommerce.exception.cartException;

public class InvalidCartOperationException extends RuntimeException{
    public InvalidCartOperationException(String message) {
        super(message);
    }
}
