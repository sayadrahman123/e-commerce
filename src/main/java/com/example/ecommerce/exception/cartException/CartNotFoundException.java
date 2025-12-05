package com.example.ecommerce.exception.cartException;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(Long userId) {
        super("Cart Not Found with id: " + userId);
    }
    public CartNotFoundException(String message) {
        super(message);
    }
}
