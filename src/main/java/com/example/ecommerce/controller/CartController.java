package com.example.ecommerce.controller;

import com.example.ecommerce.model.dto.AddItemRequest;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestBody AddItemRequest request) {
        return cartService.addProductToCart(request.getUserId(), request.getProductId(), request.getQuantity());
    }


    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long productId, @RequestBody AddItemRequest request) {
        return cartService.updateCartItem(request.getUserId(), productId, request.getQuantity());
    }


    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long productId, @RequestParam Long userId) {
        return cartService.removeCartItem(userId, productId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }
}

