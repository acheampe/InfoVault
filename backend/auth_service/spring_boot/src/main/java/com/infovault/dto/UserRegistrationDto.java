package com.infovault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// used for registration in the application - transfers registration data
public class UserRegistrationDto {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;
    private Boolean isCognitoUser = false;

    // Getters and setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsCognitoUser() {
        return isCognitoUser != null ? isCognitoUser : false; // Provide a default if null
    }

    public void setIsCognitoUser(Boolean isCognitoUser) {
        this.isCognitoUser = isCognitoUser;
    }
}
