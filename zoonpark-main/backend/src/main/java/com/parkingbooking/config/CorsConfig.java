package com.parkingbooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig - Additional CORS configuration using WebMvcConfigurer
 * 
 * This provides an additional layer of CORS configuration to ensure
 * the frontend can communicate with the backend without issues.
 * 
 * Why do we need this?
 * - Works alongside SecurityConfig's CORS configuration
 * - Ensures CORS headers are set for all endpoints
 * - Provides a fallback if SecurityConfig CORS doesn't apply
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Allow requests from frontend development servers
                .allowedOrigins(
                        "http://localhost:5173", // Vite default port
                        "http://localhost:3000", // React default port
                        "https://zoonpark.vercel.app")
                // Allow all common HTTP methods
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                // Allow all headers
                .allowedHeaders("*")
                // Allow credentials (cookies, authorization headers)
                .allowCredentials(true)
                // Cache preflight response for 1 hour
                .maxAge(3600);
    }
}
