package com.example.ecommerce.model.dto;

import com.example.ecommerce.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private List<CartItem> cartItems;
    private double totalPrice;
}
