package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;




    public ResponseEntity<?> createRazorpayOrder(Double amount, String currency) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amount * 100); // Razorpay expects amount in paise
            options.put("currency", currency);
            options.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("status", "created");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    public ResponseEntity<?> verifyAndSavePayment(Map<String, String> paymentDetails) {
        try {
            String razorpayOrderId = paymentDetails.get("razorpay_order_id");
            String razorpayPaymentId = paymentDetails.get("razorpay_payment_id");
            String razorpaySignature = paymentDetails.get("razorpay_signature");
            Long userId = Long.parseLong(paymentDetails.get("user_id"));

            String generatedSignature = generateSignature(
                    razorpayOrderId + "|" + razorpayPaymentId,
                    razorpayKeySecret
            );

            if (!generatedSignature.equals(razorpaySignature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "failed", "message", "Invalid signature"));
            }

            // 💾 Step 1: Save initial payment record (without linking order yet)
            Payment payment = new Payment();
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpaySignature(razorpaySignature);
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            // 🧩 Step 2: Place order
            ResponseEntity<?> placeOrderResponse = orderService.placeOrder(userId);
            com.example.ecommerce.model.Order savedOrder = null;

            if (placeOrderResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = (Map<String, Object>) placeOrderResponse.getBody();

                if (body != null && body.get("orderId") != null) {
                    Long orderId = Long.valueOf(body.get("orderId").toString());
                    Double totalAmount = Double.valueOf(body.get("totalAmount").toString());

                    savedOrder = orderRepository.findById(orderId).orElseThrow();

                    // 🔗 Link payment with order and amount
                    payment.setOrder(savedOrder);
                    payment.setAmount(totalAmount);
                    paymentRepository.save(payment);
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("status", "failed", "message", "Order placement failed after payment verification"));
            }

            // ✅ Step 3: Return success
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payment verified & order placed");
            response.put("orderId", savedOrder != null ? savedOrder.getId() : null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    private String generateSignature(String data, String secret) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hashBytes = mac.doFinal(data.getBytes());

        // Convert to hexadecimal string (Razorpay expects this format)
        StringBuilder hash = new StringBuilder();
        for (byte b : hashBytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }


}
