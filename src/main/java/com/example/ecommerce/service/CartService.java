package com.example.ecommerce.service;

import com.example.ecommerce.exception.cartException.CartItemNotfoundException;
import com.example.ecommerce.exception.cartException.CartNotFoundException;
import com.example.ecommerce.exception.productException.ProductNotFoundException;
import com.example.ecommerce.exception.userException.UserNotFoundException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.dto.CartResponse;
import com.example.ecommerce.model.dto.UserCart;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Transactional
@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    public ResponseEntity<?> addProductToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with Id: " + productId));

        if (product.getStock() < quantity) {
            return ResponseEntity.badRequest().body("Not enough stock!");
        }


        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue());
        } else {
            double totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity)).doubleValue();
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setTotalPrice(totalPrice);
            newItem.setCart(cart);

            cart.getCartItems().add(newItem);
        }

        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(total);

        cartRepository.save(cart);
        CartResponse response = new CartResponse(cart.getCartItems(), cart.getTotalPrice());

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> updateCartItem(Long userId, Long productId, int quantity) {

        UserCart userCart = getUserAndCart(userId);
        Cart cart = userCart.getCart();

        CartItem item = cart.getCartItems().stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotfoundException(productId));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
        } else {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(quantity))
                    .doubleValue());
        }

        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(total);

        cartRepository.save(cart);
        return ResponseEntity.ok("Cart updated successfully");
    }

    public ResponseEntity<?> removeCartItem(Long userId, Long productId) {

        UserCart userCart = getUserAndCart(userId);
        Cart cart = userCart.getCart();

        CartItem item = cart.getCartItems().stream()
                .filter(i -> Objects.equals(i.getProduct().getId(), productId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotfoundException(productId));

        cart.getCartItems().remove(item);

        if (cart.getCartItems().isEmpty()) {
            cartRepository.delete(cart);
            return ResponseEntity.ok("Cart is now empty and removed");
        }

        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(total);

        cartRepository.save(cart);

        return ResponseEntity.status(HttpStatus.OK).body("Cart item removed successfully");
    }


    public ResponseEntity<?> getUserCart(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        System.out.println(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found with ID: " + userId));
        }

        User user = optionalUser.get();

        Optional<Cart> optionalCart = cartRepository.findByUser(user);

        if (optionalCart.isEmpty()) {
            CartResponse response = new CartResponse(Collections.emptyList(), 0.0);
            return ResponseEntity.ok(response);
        }

        Cart cart = optionalCart.get();

        CartResponse response = new CartResponse(cart.getCartItems(), cart.getTotalPrice());

        return ResponseEntity.ok(response);
    }

    public UserCart getUserAndCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(userId));

        return new UserCart(user, cart);
    }

    public ResponseEntity<?> clearCart(Long userId) {

        UserCart userCart = getUserAndCart(userId);

        Cart cart = userCart.getCart();

        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);

        cartRepository.save(cart);

        return ResponseEntity.ok(Map.of(
                "message", "Cart cleared successfully",
                "cart", cart
        ));

    }

}
