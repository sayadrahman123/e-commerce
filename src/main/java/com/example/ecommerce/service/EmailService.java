package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    private final String fromEmail = "abdusrahman64@gmail.com";

    public void sendOrderConfirmation(String toEmail, String userName, String orderId, double amount) {
        String subject = "Order Confirmation - TechyTrove";
        String message = "Dear " + userName + ",\n\n" +
                "Thank you for your purchase!\n" +
                "Your order ID: " + orderId + "\n" +
                "Total Amount: ₹" + amount + "\n\n" +
                "We'll notify you once your order is shipped.\n\n" +
                "Best regards,\n" +
                "Team TechyTrove";

        System.out.println(toEmail);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        System.out.println("📧 toEmail = " + toEmail);


        try {
            javaMailSender.send(mailMessage);
            System.out.println("✅ Order confirmation email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
        }




    }
}
