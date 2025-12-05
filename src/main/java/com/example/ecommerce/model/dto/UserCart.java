package com.example.ecommerce.model.dto;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCart {
    private User user;
    private Cart cart;
}
