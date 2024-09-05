package com.infovault.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull; // Import for @NonNull annotation
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import com.infovault.service.CognitoService;

@Component
// The filter class that intercepts HTTP requests to validate JWT tokens
public class CognitoJwtAuthFilter extends OncePerRequestFilter {

    private final CognitoService cognitoService; // Service for validating JWT tokens

    // Constructor that injects the CognitoService
    public CognitoJwtAuthFilter(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
    }

    // This method is the core of the filter, where the token validation logic happens
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header from the HTTP request
        String authorizationHeader = request.getHeader("Authorization");

        // If the header is present and starts with "Bearer ", process the token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix to get the token

            // Validate the token using the CognitoService
            if (cognitoService.verifyToken(token)) {
                // If the token is valid, you would typically set an Authentication object in the SecurityContext
                // Example:
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // If the token is invalid, respond with 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return; // Stop further processing of the request
            }
        }

        // Continue the filter chain to allow the request to proceed to the next filter or resource
        filterChain.doFilter(request, response);
    }
}
