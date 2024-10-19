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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CognitoService {

    private static final Logger logger = LoggerFactory.getLogger(CognitoService.class);

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.cognito.region}")
    private String region;

    @Value("${aws.cognito.clientSecret}")
    private String clientSecret;

    private AWSCognitoIdentityProvider cognitoClient;

    @PostConstruct
    public void init() {
        cognitoClient = AWSCognitoIdentityProviderClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(region)
                .build();
    }

    private String calculateSecretHash(String username) {
        String message = username + clientId;
        SecretKeySpec signingKey = new SecretKeySpec(
            clientSecret.getBytes(StandardCharsets.UTF_8),
            "HmacSHA256"
        );

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating SECRET_HASH", e);
        }
    }

    public SignUpResult signUp(String username, String password, String email) {
        SignUpRequest request = new SignUpRequest()
            .withClientId(clientId)
            .withUsername(username)
            .withPassword(password)
            .withUserAttributes(
                new AttributeType().withName("email").withValue(email)
            )
            .withSecretHash(calculateSecretHash(username));
        return cognitoClient.signUp(request);
    }

    public InitiateAuthResult login(String username, String password) {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", username);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", calculateSecretHash(username));

        InitiateAuthRequest request = new InitiateAuthRequest()
            .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
            .withClientId(clientId)
            .withAuthParameters(authParams);

        return cognitoClient.initiateAuth(request);
    }

    public boolean verifyToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            boolean isValid = clientId.equals(jwt.getAudience().get(0));
            logger.debug("Token audience: {}, Expected clientId: {}, Is valid: {}", 
                         jwt.getAudience().get(0), clientId, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error verifying token", e);
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
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