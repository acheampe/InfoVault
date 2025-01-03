package com.infovault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.infovault.filter.CognitoJwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;


@Configuration // Marks this class as a configuration class for Spring
@EnableWebSecurity
// plays a role in setting up security infrastructure for the application, defining which endpoints are secured and how requests are authenticated
public class SecurityConfig {

    @Autowired // Injects the CognitoJwTAuthFilter into this class
    private CognitoJwtAuthFilter cognitoJwtAuthFilter;

    @Bean // Defines a bean for configuring the security filter chain
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Updated way to disable CSRF
            .authorizeHttpRequests(auth -> auth // Updated way to authorize HTTP requests
                .requestMatchers("auth/", "/auth/register", "/auth/login").permitAll() 
                .requestMatchers("/auth/user", "/auth/logout").authenticated() // Requires authentication for all other requests
                .anyRequest().authenticated() // Requires authentication for all other requests
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // set to stateless
            .addFilterBefore(cognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Authentication failed: " + authException.getMessage());
                })
            );
        return http.build(); // Builds and returns the SecurityFilterChain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}