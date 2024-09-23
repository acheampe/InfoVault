package com.infovault.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Java package used for mapping Java objects to database tables
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity // Maps class to a database table
@Table(name = "users") // Specifies the table name in the database
public class User {

    @Id // Marks Id field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the Id field is auto-generated by the database
    private Long userId; // Stores unique identifier for user

    // Stores the rest of the user's pertinent info
    @Column(nullable = false) // Specifies that the firstName field cannot be null
    private String firstName;

    @Column(nullable = false) // Specifies that the lastName field cannot be null
    private String lastName;

    @Column(nullable = false, unique = true) // Specifies that the email field must be unique and cannot be null
    private String email;

    @Column(nullable = false) // Specifies that the phoneNumber field cannot be null
    private String phoneNumber;

    @Column(nullable = false) // Specifies that the passwordHash field cannot be null
    private String passwordHash; // Password should be encrypted in a real application

    @Column(nullable = false) // Specifies that the isFederated field cannot be null
    private Boolean isFederated = false; // Indicates if the user is a federated user

    @Column(nullable = false) // Specifies that the createdAt field cannot be null
    private LocalDateTime createdAt = LocalDateTime.now(); // Stores the timestamp when the user was created

    private LocalDateTime lastLogin; // Stores the timestamp of the user's last login

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Method to set and hash the password for a user
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = hashPassword(passwordHash);
    }

    // Method to hash the password
    private String hashPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Boolean getIsFederated() {
        return isFederated;
    }

    public void setIsFederated(Boolean isFederated) {
        this.isFederated = isFederated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
