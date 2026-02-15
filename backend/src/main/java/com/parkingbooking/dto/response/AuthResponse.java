package com.parkingbooking.dto.response;

/**
 * AuthResponse DTO - Response after successful login/registration
 * 
 * Contains:
 * - JWT token (used for authentication in subsequent requests)
 * - User information
 * 
 * The frontend will:
 * 1. Store the token (usually in localStorage)
 * 2. Include it in Authorization header for protected requests
 */
public class AuthResponse {

    private String token;
    private String type = "Bearer"; // Token type
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private Boolean isApproved;

    // Constructors
    public AuthResponse() {
    }

    public AuthResponse(String token, Long id, String email, String fullName, String role, Boolean isApproved) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isApproved = isApproved;
    }

    public AuthResponse(String token, String type, Long id, String email, String fullName, String role,
            Boolean isApproved) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isApproved = isApproved;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
