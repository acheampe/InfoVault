package com.infovault.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.infovault.dto.UserRegistrationDto;
import com.infovault.model.User;
import com.infovault.repository.UserRepository;
import com.infovault.security.JwtTokenProvider;
import com.infovault.exception.UserAlreadyExistsException;
import com.infovault.exception.InvalidInputException;
import com.infovault.exception.UserNotFoundException;
import com.infovault.exception.AuthenticationException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.infovault.exception.CognitoRegistrationException;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CognitoService cognitoService;

    @Transactional
    public User registerNewUser(UserRegistrationDto registrationDto) {
        logger.info("Attempting to register new user with email: {}", registrationDto.getEmail());
        validateRegistrationInput(registrationDto);

        // Check if user exists in Cognito first
        if (registrationDto.getIsCognitoUser() && cognitoService.doesUserExist(registrationDto.getEmail())) {
            logger.warn("User already exists in Cognito: {}", registrationDto.getEmail());
            throw new UserAlreadyExistsException("User already exists in Cognito - Registration failed, please login");
        }

        User user = new User();
        user.setFirstName(registrationDto.getFirstName().trim());
        user.setLastName(registrationDto.getLastName().trim());
        user.setEmail(registrationDto.getEmail().toLowerCase());
        if (registrationDto.getPhoneNumber() != null && !registrationDto.getPhoneNumber().trim().isEmpty()) {
            user.setPhoneNumber(registrationDto.getPhoneNumber().trim());
        } else {
            user.setPhoneNumber(null);
        }
        user.setIsCognitoUser(registrationDto.getIsCognitoUser());

        if (registrationDto.getIsCognitoUser()) {
            try {
                SignUpResult signUpResult = cognitoService.signUp(user.getEmail(), registrationDto.getPassword(), user.getEmail());
                user.setCognitoUsername(signUpResult.getUserSub());
            } catch (UserAlreadyExistsException e) {
                logger.warn("User already exists in Cognito: {}", user.getEmail());
                throw e;
            } catch (CognitoRegistrationException e) {
                logger.error("Failed to register user with Cognito", e);
                throw e;
            }
        } else {
            user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        }
        
        User savedUser = userRepository.save(user);
        logger.info("New user registered successfully: {}", savedUser.getEmail());
        return savedUser;
    } 
    
    private void validateRegistrationInput(UserRegistrationDto registrationDto) {
        if (registrationDto.getEmail() == null || !isValidEmail(registrationDto.getEmail())) {
            throw new InvalidInputException("Invalid email address: " + registrationDto.getEmail());
        }
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + registrationDto.getEmail() + " already exists");
        }
        if (registrationDto.getPhoneNumber() != null && !registrationDto.getPhoneNumber().trim().isEmpty()) {
            if (!isValidPhoneNumber(registrationDto.getPhoneNumber().trim())) {
                throw new InvalidInputException("Invalid phone number: " + registrationDto.getPhoneNumber());
            }
        }
        if (registrationDto.getPassword() == null || !isValidPassword(registrationDto.getPassword())) {
            throw new InvalidInputException("Password must have at least 8 characters, including uppercase and lowercase letters, numbers, and special characters");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    // Update the isValidPhoneNumber method to be more lenient
    private boolean isValidPhoneNumber(String phoneNumber) {
        // This regex allows for various phone number formats
        // It's less strict than the previous version
        String phoneRegex = "^\\+?[0-9]{7,15}$";
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isValidPassword(String password) {
        // Updated regex to match your Cognito password policy
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

    public boolean isCognitoUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getIsCognitoUser();
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
