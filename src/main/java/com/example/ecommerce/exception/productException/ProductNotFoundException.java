package com.example.ecommerce.exception.productException;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long productId) {
        super("Product Not Found with id: " + productId);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

}
