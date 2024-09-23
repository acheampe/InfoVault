package com.infovault;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class InfoVaultApplicationTests {

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot be loaded
    }
}