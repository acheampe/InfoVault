package com.infovault.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service // Marks class as a Spring Service, a type of Spring managed bean
public class CognitoService {

    private static final Logger logger = LoggerFactory.getLogger(CognitoService.class);

    @Value("${aws.cognito.userPoolId}") // Injects the value of 'aws.cognito.userPoolId' from application.properties
    private String userPoolId;

    @Value("${aws.cognito.clientId}") // Injects the value of 'aws.cognito.clientId' from application.properties
    private String clientId;

    @Value("${aws.cognito.region}") // Injects the value of 'aws.cognito.region' from application.properties
    private String region;

    private AWSCognitoIdentityProvider cognitoClient;

    // Initializes the Cognito client after construction
    @PostConstruct
    public void init() {
        cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(region)
                .build();
    }

    // This method handles user registration
    public SignUpResult signUp(String username, String password, String email) {
        SignUpRequest request = new SignUpRequest()
            .withClientId(clientId)
            .withUsername(username)
            .withPassword(password)
            .withUserAttributes(
                new AttributeType().withName("email").withValue(email)
            );
        return cognitoClient.signUp(request);
    }

    // This method handles user login
    public InitiateAuthResult login(String username, String password) {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", username);
        authParams.put("PASSWORD", password);

        InitiateAuthRequest request = new InitiateAuthRequest()
            .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
            .withClientId(clientId)
            .withAuthParameters(authParams);

        return cognitoClient.initiateAuth(request);
    }

    // This method verifies the JWT token passed to it
    public boolean verifyToken(String token) {
        try {
            // Decodes JWT token but does NOT validate the token
            DecodedJWT jwt = JWT.decode(token);

            // Compares clientId from the decoded token with the configured clientId
            // If they match, then token was issued for infovault app
            boolean isValid = clientId.equals(jwt.getAudience().get(0));
            logger.debug("Token audience: {}, Expected clientId: {}, Is valid: {}", 
                         jwt.getAudience().get(0), clientId, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error verifying token", e);
            return false;
        }
    }

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