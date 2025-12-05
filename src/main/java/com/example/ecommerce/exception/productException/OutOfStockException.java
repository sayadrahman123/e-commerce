package com.example.ecommerce.exception.productException;

public class OutOfStockException extends RuntimeException{
    public OutOfStockException(String productName) {
        super("Product '" + productName + "' is out of stock.");
    }

    public OutOfStockException(String productName, int availableStock) {
        super("Product '" + productName + "' has only " + availableStock + " units left.");
    }
}
