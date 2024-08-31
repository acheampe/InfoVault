package com.infovault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a configuration class for Spring
public class SecurityConfig {

    @Autowired // Injects the custom Cognito JWT authentication filter into this class
    private CognitoJwtAuthFilter cognitoJwtAuthFilter;

    @Bean // Defines a bean for configuring the security filter chain
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disables Cross-Site Request Forgery (CSRF) protection, done in stateless APIs using JWT
            .authorizeRequests() // Begins to define URL paths that should be secure and which should be public
                .antMatchers("/public/**").permitAll() // Allows all requests to paths under /public/ without authentication
                .anyRequest().authenticated() // Requires authentication for all other requests
            .and() // Continues HTTP security configuration
            .addFilterBefore(cognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adds the custom Cognito JWT authentication filter before the default UsernamePasswordAuthenticationFilter

        return http.build(); // Builds and returns the SecurityFilterChain
    }

    @Bean // Defines a bean for the custom Cognito JWT authentication filter
    public CognitoJwtAuthFilter cognitoJwtAuthFilter() throws Exception {
        return new CognitoJwtAuthFilter(); // Creates and returns a new instance of the custom Cognito JWT authentication filter
    }
}
