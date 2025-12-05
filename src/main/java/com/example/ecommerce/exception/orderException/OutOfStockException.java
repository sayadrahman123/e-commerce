package com.example.ecommerce.exception.orderException;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String productName) {
        super("Product '" + productName + "' is out of stock.");
    }
}
