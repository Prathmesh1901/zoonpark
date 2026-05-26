package com.parkingbooking.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * User Entity - Represents all users in the system
 * 
 * This entity stores information for:
 * - Customers (who book parking spaces)
 * - Owners (who provide parking spaces)
 * - Admins (who manage the platform)
 * 
 * The role field determines what type of user this is
 * 
 * Key Features:
 * - Email is unique (used for login)
 * - Password is encrypted (BCrypt)
 * - Owners need admin approval before they can add parking spaces
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email address - used for login
     * Must be unique across all users
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Encrypted password (BCrypt hash)
     * Never store plain text passwords!
     */
    @Column(nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    /**
     * User's full name
     */
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    /**
     * Contact phone number
     */
    @Column(length = 15)
    private String phone;

    /**
     * User's role (CUSTOMER, OWNER, or ADMIN)
     * 
     * @ManyToOne - Many users can have the same role
     * @JoinColumn - Specifies the foreign key column
     *             fetch = FetchType.EAGER - Load role data immediately with user
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Approval status for OWNER users
     * - true: Owner can add parking spaces
     * - false: Owner is pending approval or blocked
     * - Not applicable for CUSTOMER and ADMIN roles
     */
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    /**
     * Account creation timestamp
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically set creation timestamp before saving
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Auto-approve customers and admins
        if (role != null && (role.getName().equals("CUSTOMER") || role.getName().equals("ADMIN"))) {
            isApproved = true;
        }
    }

    // Constructors
    public User() {
    }

    public User(Long id, String email, String password, String fullName, String phone,
            Role role, Boolean isApproved, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.isApproved = isApproved;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
