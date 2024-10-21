package com.infovault;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.infovault.service.CognitoService;

@SpringBootTest
@ActiveProfiles("test")
class InfoVaultApplicationTests {

    @MockBean
    private CognitoService cognitoService;

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot be loaded
    }
}