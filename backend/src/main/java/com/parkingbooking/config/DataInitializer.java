package com.parkingbooking.config;

import com.parkingbooking.entity.Role;
import com.parkingbooking.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataInitializer - Initializes database with default roles
 * 
 * This class runs automatically when the application starts
 * It creates the three default roles if they don't exist:
 * - CUSTOMER
 * - OWNER
 * - ADMIN
 * 
 * CommandLineRunner - Interface that runs code after Spring Boot starts
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Run method - executed on application startup
     * 
     * @param args Command line arguments
     */
    @Override
    public void run(String... args) {
        // Create CUSTOMER role if it doesn't exist
        if (!roleRepository.existsByName("CUSTOMER")) {
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            customerRole.setDescription("Regular user who books parking spaces");
            roleRepository.save(customerRole);
            System.out.println("✅ Created CUSTOMER role");
        }

        // Create OWNER role if it doesn't exist
        if (!roleRepository.existsByName("OWNER")) {
            Role ownerRole = new Role();
            ownerRole.setName("OWNER");
            ownerRole.setDescription("Parking space owner who provides parking");
            roleRepository.save(ownerRole);
            System.out.println("✅ Created OWNER role");
        }

        // Create ADMIN role if it doesn't exist
        if (!roleRepository.existsByName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator who manages the platform");
            roleRepository.save(adminRole);
            System.out.println("✅ Created ADMIN role");
        }

        System.out.println("🎉 Database initialization complete!");
    }
}
