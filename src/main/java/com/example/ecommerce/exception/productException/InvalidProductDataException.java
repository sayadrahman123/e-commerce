package com.example.ecommerce.exception.productException;

public class InvalidProductDataException extends RuntimeException{
    public InvalidProductDataException(String message) {
        super("Invalid Product Data: " + message);
    }
}
