package com.infovault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.infovault.model.User;
import com.infovault.repository.UserRepository;
import com.infovault.security.JwtTokenProvider; 

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenProvider.generateToken(user);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
}