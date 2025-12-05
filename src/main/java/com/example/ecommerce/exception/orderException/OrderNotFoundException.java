package com.example.ecommerce.exception.orderException;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long orderId) {
        super("Order Not Found with id: " + orderId);
    }
    public OrderNotFoundException(String message) {
        super(message);
    }
}
