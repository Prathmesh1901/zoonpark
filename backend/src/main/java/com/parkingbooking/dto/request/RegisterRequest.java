package com.parkingbooking.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest DTO - Data Transfer Object for user registration
 * 
 * DTOs are used to transfer data between client and server
 * They are separate from entities to:
 * 1. Hide sensitive fields (like password hash)
 * 2. Add validation rules
 * 3. Control what data is sent/received
 * 
 * Validation Annotations:
 * 
 * @NotBlank - Field cannot be null or empty
 * @Email - Must be valid email format
 * @Size - Restricts string length
 */
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String fullName;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;

    @NotBlank(message = "Role is required")
    private String role; // "CUSTOMER", "OWNER", or "ADMIN"

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String fullName, String phone, String role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
