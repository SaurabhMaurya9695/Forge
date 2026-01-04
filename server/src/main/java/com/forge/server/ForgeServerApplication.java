package com.forge.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Forge Server - Main Entry Point
 * <p>
 * This is the single entry point for the Forge CI/CD Platform Control Plane.
 * It initializes the Spring Boot application and starts all configured services.
 *
 * @author Forge Team
 */
@SpringBootApplication
public class ForgeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForgeServerApplication.class, args);
    }
}

