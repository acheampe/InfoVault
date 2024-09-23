package com.infovault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import io.github.cdimascio.dotenv.Dotenv;
import java.nio.file.Paths;

@SpringBootApplication
@EntityScan("com.infovault.model")
@EnableJpaRepositories("com.infovault.repository")
public class InfoVaultApplication {

    static {
        try {
            String projectRoot = System.getProperty("user.dir");
            String envPath = Paths.get(projectRoot, "..", ".env").normalize().toString();
            Dotenv dotenv = Dotenv.configure()
                    .directory(Paths.get(projectRoot, "..").normalize().toString())
                    .load();
            System.out.println("Loaded .env file from: " + envPath);
            
            // Set all environment variables as system properties
            dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
            
            // Optionally, you can print out a test environment variable to verify it's loaded
            System.out.println("Test ENV variable: " + dotenv.get("TEST_VARIABLE"));
        } catch (Exception e) {
            System.err.println("Warning: Failed to load .env file. Using system environment variables.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(InfoVaultApplication.class, args);
    }
}