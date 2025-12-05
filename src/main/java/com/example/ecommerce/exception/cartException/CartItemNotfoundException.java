package com.example.ecommerce.exception.cartException;

public class CartItemNotfoundException extends RuntimeException{
    public CartItemNotfoundException(Long productId) {
        super("Item with product ID " + productId + " not found in cart.");
    }
}
