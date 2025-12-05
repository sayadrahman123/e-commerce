package com.example.ecommerce.exception.userException;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
