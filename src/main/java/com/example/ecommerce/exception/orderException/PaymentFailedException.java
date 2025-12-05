package com.example.ecommerce.exception.orderException;

public class PaymentFailedException extends RuntimeException{
    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException() {
        super("Payment Processing Failed. Please try again later");
    }
}
