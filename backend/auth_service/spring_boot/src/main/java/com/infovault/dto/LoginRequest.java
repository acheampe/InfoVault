package com.infovault.dto;

public class LoginRequest {
   private String email;
   private String password;

   // Getters and Setters - defines log in request structure
   public String getEmail() {
        return email;
   }

   public void setEmail(String email) {
    this.email = email;
   }

   public String getPassword() {
    return password;
   }

   public void setPassword(String password) {
    this.password = password;
    
   }

}
