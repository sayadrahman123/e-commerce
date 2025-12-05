package com.example.ecommerce.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemRequest {
    private long userId;
    private long productId;
    private int quantity;
}
