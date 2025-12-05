package com.example.ecommerce.exception.userException;

public class UnauthorizedAccessException extends RuntimeException{

    public UnauthorizedAccessException() {
        super("You are not authorized to perform this action");
    }
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
