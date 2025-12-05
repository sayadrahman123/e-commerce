package com.example.ecommerce.service;

import com.example.ecommerce.exception.orderException.OrderNotFoundException;
import com.example.ecommerce.exception.userException.UserNotFoundException;
import com.example.ecommerce.model.*;
import com.example.ecommerce.model.dto.OrderResponse;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ResponseEntity<?> placeOrder(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
        User currentUser = user.get();

        Optional<Cart> cart = cartRepository.findByUser(currentUser);

        if (cart.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for user with ID: " + userId);
        }
        Cart currentCart = cart.get();

        if (currentCart.getCartItems().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Your cart is empty, cannot proceed to checkout.");
        }

        double totalPrice = currentCart.getTotalPrice();

        Order order = new Order();

        order.setUser(currentUser);
        order.setTotalAmount(currentCart.getTotalPrice());
        order.setOrderStatus("PLACED");
        order.setPaymentStatus("SUCCESS");



        emailService.sendOrderConfirmation(currentUser.getEmail(), currentUser.getUsername(), String.valueOf(order.getId()), currentCart.getTotalPrice());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : currentCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItems.add(orderItem);
        }

        for (OrderItem orderItem : orderItems) {
            productService.updateProductStock(orderItem.getProduct().getId(), orderItem.getProduct().getStock() - orderItem.getQuantity());
        }

        order.setOrderItems(orderItems);

        orderRepository.save(order);
        cartService.clearCart(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order placed successfully");
        response.put("orderId", order.getId());
        response.put("totalAmount", order.getTotalAmount());
        response.put("paymentStatus", order.getPaymentStatus());
        return ResponseEntity.ok(response);


    }

    public List<OrderResponse> getOrderResponse(List<Order> orders) {
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            OrderResponse orderResponse = new OrderResponse(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getPaymentStatus(),
                    order.getOrderStatus(),
                    order.getOrderItems()
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }


    public ResponseEntity<?> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Order> orders = orderRepository.findByUser(user);
        List<OrderResponse> orderResponses = getOrderResponse(orders);


        Map<String, Object> response = new HashMap<>();
        response.put("message", "Orders retrieved successfully");
        response.put("orders", orderResponses);

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = getOrderResponse(orders);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Orders retrieved successfully");
        response.put("orders", orderResponses);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        List<Order> orders = new ArrayList<>();
        orders.add(order);
        List<OrderResponse> orderResponse = getOrderResponse(orders);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order retrieved successfully");
        response.put("order", orderResponse);

        return ResponseEntity.ok(response);
    }


}
