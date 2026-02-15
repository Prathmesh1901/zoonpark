package com.parkingbooking.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Role Entity - Represents user roles in the system
 * 
 * This entity stores different user roles: CUSTOMER, OWNER, ADMIN
 * Each user is assigned one role which determines their permissions
 * 
 * JPA Annotations:
 * 
 * @Entity - Marks this class as a JPA entity (database table)
 * @Table - Specifies the table name in database
 * @Id - Marks the primary key field
 * @GeneratedValue - Auto-generates the ID value
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role name: CUSTOMER, OWNER, or ADMIN
     * 
     * @Column(unique = true) ensures no duplicate role names
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Description of what this role can do
     */
    @Column(length = 255)
    private String description;

    /**
     * Timestamp when this role was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically set creation timestamp before persisting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Role() {
    }

    public Role(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
