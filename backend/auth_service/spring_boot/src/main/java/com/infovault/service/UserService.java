package com.infovault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infovault.model.User;
import com.infovault.repository.UserRepository;

@Service // marks class as a service to inject into othert components
public class UserService {

    @Autowired // marks field as needing dependency injection
    private UserRepository userRepository;

    // Method to register a user
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Additional methods for user authentication should be added here later
}