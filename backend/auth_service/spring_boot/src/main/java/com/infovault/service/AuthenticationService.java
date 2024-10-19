package com.infovault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.infovault.model.User;
import com.infovault.repository.UserRepository;
import com.infovault.exception.AuthenticationException;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.infovault.security.JwtTokenProvider;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CognitoService cognitoService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationException("User not found with email: " + email));

        if (user.getIsCognitoUser()) {
            return authenticateWithCognito(email, password);
        } else {
            return authenticateWithLocalDatabase(user, password);
        }
    }

    private String authenticateWithCognito(String email, String password) {
        try {
            InitiateAuthResult authResult = cognitoService.login(email, password);
            if (authResult.getAuthenticationResult() != null) {
                return authResult.getAuthenticationResult().getIdToken();
            } else {
                throw new AuthenticationException("Cognito authentication failed");
            }
        } catch (Exception e) {
            throw new AuthenticationException("Cognito authentication failed: " + e.getMessage());
        }
    }

    private String authenticateWithLocalDatabase(User user, String password) {
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return jwtTokenProvider.generateToken(user);
        } else {
            throw new AuthenticationException("Invalid password for user: " + user.getEmail());
        }
    }
}