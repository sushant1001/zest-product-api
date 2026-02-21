package com.zest.products.config;

import com.zest.products.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AuthService authService) {
        return args -> {

            try {
                authService.registerUser("admin", "admin123", "ROLE_ADMIN");
                System.out.println("Default admin user created - username: admin, password: admin123");
            } catch (RuntimeException e) {

                System.out.println("Admin user already exists");
            }

            try {
                authService.registerUser("user", "user123", "ROLE_USER");
                System.out.println("Default user created - username: user, password: user123");
            } catch (RuntimeException e) {
                System.out.println("Default user already exists");
            }
        };
    }
}
