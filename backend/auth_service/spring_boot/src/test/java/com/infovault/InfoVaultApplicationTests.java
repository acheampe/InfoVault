package com.infovault; // Ensure this matches the package of your main application class

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = InfoVaultApplication.class) // Explicitly specify the main application class
class InfoVaultApplicationTests {

    @Test
    void contextLoads() {
        // This test simply checks if the Spring application context loads correctly
    }

}
