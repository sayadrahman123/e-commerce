package com.example.ecommerce.exception.userException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long userId) {
        super("User Not Found with id: " + userId);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
