package com.example.ecommerce.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
