package com.example.ecommerce.exception.orderException;

public class OrderProcessingException extends RuntimeException{
    public OrderProcessingException(String message) {
        super("Error occurred while processing order: " + message);
    }
}
