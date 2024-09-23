package com.infovault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infovault.dto.UserRegistrationDto;
import com.infovault.model.User;
import com.infovault.repository.UserRepository;
import com.infovault.security.JwtTokenProvider;
import com.infovault.exception.UserAlreadyExistsException;
import com.infovault.exception.InvalidInputException;
import com.infovault.exception.UserNotFoundException;
import com.infovault.exception.AuthenticationException;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        logger.info("Attempting to register new user with email: {}", registrationDto.getEmail());
        validateRegistrationInput(registrationDto);

        User user = new User();
        user.setFirstName(registrationDto.getFirstName().trim());
        user.setLastName(registrationDto.getLastName().trim());
        user.setEmail(registrationDto.getEmail().toLowerCase());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setIsFederated(registrationDto.getIsFederated() != null ? registrationDto.getIsFederated() : false);

        User savedUser = userRepository.save(user);
        logger.info("New user registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    private void validateRegistrationInput(UserRegistrationDto registrationDto) {
        if (registrationDto.getFirstName() == null || registrationDto.getFirstName().trim().isEmpty()) {
            throw new InvalidInputException("First name is required");
        }
        if (registrationDto.getLastName() == null || registrationDto.getLastName().trim().isEmpty()) {
            throw new InvalidInputException("Last name is required");
        }
        if (registrationDto.getEmail() == null || !isValidEmail(registrationDto.getEmail())) {
            throw new InvalidInputException("Invalid email address: " + registrationDto.getEmail());
        }
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + registrationDto.getEmail() + " already exists");
        }
        if (registrationDto.getPhoneNumber() == null || !isValidPhoneNumber(registrationDto.getPhoneNumber())) {
            throw new InvalidInputException("Invalid phone number: " + registrationDto.getPhoneNumber());
        }
        if (registrationDto.getPassword() == null || !isValidPassword(registrationDto.getPassword())) {
            throw new InvalidInputException("Password does not meet security requirements");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{10,14}$";
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }

    public String loginUser(String email, String password) {
        logger.info("Attempting login for user: {}", email);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            String token = jwtTokenProvider.generateToken(user);
            logger.info("User logged in successfully: {}", email);
            return token;
        } else {
            logger.warn("Failed login attempt for user: {}", email);
            throw new AuthenticationException("Invalid password for user: " + email);
        }
    }

    public User findByUsername(String email) {
        logger.info("Searching for user with email: {}", email);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}