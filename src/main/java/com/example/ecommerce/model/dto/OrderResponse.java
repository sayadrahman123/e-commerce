package com.example.ecommerce.model.dto;

import com.example.ecommerce.model.OrderItem;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private double totalAmount;
    private String paymentStatus;
    private String orderStatus;
    private List<OrderItem> orderItems;
}
