package com.infovault.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infovault.model.User;
import com.infovault.service.UserService;
import com.infovault.dto.RegistrationResponse;
import com.infovault.dto.LoginRequest;
import com.infovault.dto.LoginResponse;


@RestController // marks class as a controller where every method returns a domain object instead of a view
@RequestMapping("/auth") // Base URL for this controller
public class AuthController {

    @Autowired // marks field as needing dependency injection
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);

        return ResponseEntity.ok(new RegistrationResponse("User registered successfully", registeredUser.getId()));
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
