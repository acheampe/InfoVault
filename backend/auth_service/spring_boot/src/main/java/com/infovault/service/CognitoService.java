package com.infovault.service;

import org.slf4j.LoggerFactory;
// injects AWS cognito 'clientId' from your config
import org.springframework.beans.factory.annotation.Value; 
// marks a class as a service layer for springboot to manage its services
import org.springframework.stereotype.Service;
// To allow DecodedJWT to hold decoded token and retrieve its claim
import com.auth0.jwt.interfaces.DecodedJWT;
// Used to decoded JWT string
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.slf4j.Logger;

@Service // Marks class as a Spring Service, a type of Spring managed bean
public class CognitoService{

    @Value("${aws.cognito.clientId}") // Injects the value of 'aws.cognito.clientId' from application.properties  
    private String clientId; 

    // This method verifies the JWT token passed to it
    public boolean verifyToken(String token) {
        // Decodes JWT token but does NOT validate the token
        DecodedJWT jwt = JWT.decode(token); 

        // Compares clientId from the decoded token with the configured clientId
        // If they match, then token was issued for infovault app
        return clientId.equals(jwt.getAudience().get(0));
    }

    private static final Logger logger = LoggerFactory.getLogger(CognitoService.class);

    // This method extracts the username from the JWT token
    public String getUsernameFromToken(String token) {
        try {
            // Decodes JWT token
            DecodedJWT jwt = JWT.decode(token);
            
            // Retrieves the 'cognito:username' claim from the token
            String username = jwt.getClaim("cognito:username").asString();
            
            if (username == null || username.isEmpty()) {
                logger.warn("Username claim not found in token");
                throw new IllegalArgumentException("Username claim not found in token");
            }
            
            return username;
        } catch (JWTDecodeException e) {
            logger.error("Failed to decode JWT token", e);
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }
}
