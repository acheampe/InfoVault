package com.infovault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.infovault.filter.CognitoJwtAuthFilter;

@Configuration // Marks this class as a configuration class for Spring
public class SecurityConfig {

    @Autowired // Injects the CognitoJwTAuthFilter into this class
    private CognitoJwtAuthFilter cognitoJwtAuthFilter;

    @Bean // Defines a bean for configuring the security filter chain
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Updated way to disable CSRF
            .authorizeHttpRequests(auth -> auth // Updated way to authorize HTTP requests
                .requestMatchers("/", "/public/**").permitAll() // Allows all requests to paths under /public/ without authentication
                .anyRequest().authenticated() // Requires authentication for all other requests
            )
            .addFilterBefore(cognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Builds and returns the SecurityFilterChain
    }

}
