package com.parkingbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for Parking Booking System
 * 
 * @SpringBootApplication is a convenience annotation that combines:
 *                        - @Configuration: Marks this class as a source of bean
 *                        definitions
 *                        - @EnableAutoConfiguration: Enables Spring Boot's
 *                        auto-configuration
 *                        - @ComponentScan: Scans for components in this package
 *                        and sub-packages
 * 
 *                        This is the entry point of the Spring Boot
 *                        application.
 */
@SpringBootApplication
public class ParkingBookingApplication {

    /**
     * Main method that starts the Spring Boot application
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ParkingBookingApplication.class, args);
        System.out.println("🚀 Parking Booking System Started Successfully!");
    }

    /**
     * Auto-approve all existing users on startup (FOR DEVELOPMENT ONLY)
     * This ensures you don't need to manually approve users in the database
     */
    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner autoApproveUsers(
            com.parkingbooking.repository.UserRepository userRepository) {
        return args -> {
            java.util.List<com.parkingbooking.entity.User> users = userRepository.findAll();
            int updatedCount = 0;
            for (com.parkingbooking.entity.User user : users) {
                if (user.getIsApproved() == null || !user.getIsApproved()) {
                    user.setIsApproved(true);
                    userRepository.save(user);
                    updatedCount++;
                }
            }
            if (updatedCount > 0) {
                System.out.println("✅ Auto-approved " + updatedCount + " existing users (Development Mode)");
            }
        };
    }
}
