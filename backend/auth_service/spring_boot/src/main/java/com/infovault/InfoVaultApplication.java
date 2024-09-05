package com.infovault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.infovault.model") // Explicitly define the package to scan for entities
@EnableJpaRepositories(basePackages = "com.infovault.repository")
public class InfoVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfoVaultApplication.class, args);
	}

}
