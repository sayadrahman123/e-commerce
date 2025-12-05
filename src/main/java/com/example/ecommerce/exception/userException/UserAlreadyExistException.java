package com.example.ecommerce.exception.userException;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String username) {
        super("User '" + username + "' already exists.");
    }
}
