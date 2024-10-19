package com.infovault.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.infovault.model.User;
import com.infovault.service.UserService;
import com.infovault.dto.LoginRequest;
import com.infovault.dto.LoginResponse;
import com.infovault.dto.UserRegistrationDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.http.HttpStatus;
import com.infovault.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.infovault.service.AuthenticationService;
import com.infovault.service.CognitoService;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CognitoService cognitoService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        boolean isCognitoUser = registrationDto.getIsCognitoUser() != null ? registrationDto.getIsCognitoUser() : false;
        if (isCognitoUser) {
            try {
                System.out.println("Registering Cognito user: " + registrationDto.getEmail());
                cognitoService.signUp(registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getEmail());
                System.out.println("Cognito registration successful");
                
                // Ensure the user is also added to the local database
                System.out.println("Attempting to add user to local database");
                User localUser = userService.registerNewUser(registrationDto);
                System.out.println("Local database registration successful: " + localUser.getUserId());
                
                return ResponseEntity.ok("User registered successfully with Cognito and local database");
            } catch (Exception e) {
                System.err.println("Registration failed: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
            }
        } else {
            userService.registerNewUser(registrationDto);
            return ResponseEntity.ok("User registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("Login Request - Email: " + loginRequest.getEmail() + ", Password: " + loginRequest.getPassword());
        
        try {
            String token;
            if (userService.isCognitoUser(loginRequest.getEmail())) {
                // Authenticate with Cognito
                InitiateAuthResult result = cognitoService.login(loginRequest.getEmail(), loginRequest.getPassword());
                token = result.getAuthenticationResult().getIdToken();
            } else {
                // Authenticate with local database
                token = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
            }
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    
        String username = authentication.getName();
        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    
        try {
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            // For Cognito users, token invalidation should be handled on the client side
            // Here we just clear the server-side session
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}