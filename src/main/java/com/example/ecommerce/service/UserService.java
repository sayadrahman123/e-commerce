package com.example.ecommerce.service;

import com.example.ecommerce.exception.userException.UserNotFoundException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.dto.UserResponseDto;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow( () -> new UserNotFoundException("User not found with username: " + email));
        if (user != null) {
            UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getRole().name());
            return ResponseEntity.ok(userResponseDto);
        }
        return ResponseEntity.notFound().build();
    }
}
