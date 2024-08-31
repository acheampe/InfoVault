package com.infovault.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import com.infovault.service.CognitoService;

public class CognitoJwtAuthFilter extends OncePerRequestFilter {

    private final CognitoService cognitoService;

    public CognitoJwtAuthFilter(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (cognitoService.verifyToken(token)) {
                // If token is valid, set Authentication object in SecurityContext
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
