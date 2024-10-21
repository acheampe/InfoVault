package com.infovault.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.infovault.service.CognitoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Component
// Filter class to intercept HTTP requests and validate JWT tokens
public class CognitoJwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CognitoJwtAuthFilter.class);
    private final CognitoService cognitoService; // Service for validating JWT tokens

    // Constructor injecting CognitoService
    public CognitoJwtAuthFilter(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
    }

    // Core method for token validation logic
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Request method: {}", request.getMethod());

        // Allows for registration and login without authentication
        if (request.getRequestURI().equals("/auth/register") || request.getRequestURI().equals("/auth/login")) {
            logger.debug("Skipping authentication for {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Authorization header for all other requests
        String authorizationHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authorizationHeader);

        // Process token if header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            logger.debug("Extracted token: {}", token);

            try {
                // Validate token using CognitoService
                if (cognitoService.verifyToken(token)) {
                    String username = cognitoService.getUsernameFromToken(token);
                    logger.debug("Token verified for user: {}", username);

                    // Set Authentication object in SecurityContext
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Respond with 401 Unauthorized if token is invalid
                    logger.warn("Invalid token received");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (Exception e) {
                // Handle token processing errors
                logger.error("Error processing token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            logger.debug("No Authorization header found");
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}