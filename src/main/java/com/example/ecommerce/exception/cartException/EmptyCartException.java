package com.example.ecommerce.exception.cartException;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException() {
        super("Your cart is empty. Please add items before checkout.");
    }

}
