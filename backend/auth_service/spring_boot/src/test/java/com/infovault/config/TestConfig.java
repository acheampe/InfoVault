package com.infovault.config;

import com.infovault.service.CognitoService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public CognitoService mockCognitoService() {
        return Mockito.mock(CognitoService.class);
    }
}