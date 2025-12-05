package com.example.ecommerce.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String firstName;

    private String lastName;
    private String username;
    private String role;
}
