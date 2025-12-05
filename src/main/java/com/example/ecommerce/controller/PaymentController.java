package com.example.ecommerce.controller;

import com.example.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;



    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        Double amount = Double.parseDouble(request.get("amount").toString());
        String currency = request.getOrDefault("currency", "INR").toString();
        return paymentService.createRazorpayOrder(amount, currency);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> paymentDetails) {
        return paymentService.verifyAndSavePayment(paymentDetails);
    }

}
