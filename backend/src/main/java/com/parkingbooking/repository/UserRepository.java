package com.parkingbooking.repository;

import com.parkingbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Data access layer for User entity
 * 
 * Provides methods to interact with the users table
 * Includes custom queries for user management
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email (used for login)
     * 
     * Generated query: SELECT * FROM users WHERE email = ?
     * 
     * @param email User's email address
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if email already exists (for registration validation)
     * 
     * Generated query: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find all users by role name
     * 
     * This uses a JOIN between users and roles tables
     * Generated query:
     * SELECT u.* FROM users u
     * INNER JOIN roles r ON u.role_id = r.id
     * WHERE r.name = ?
     * 
     * @param roleName Role name (e.g., "OWNER")
     * @return List of users with that role
     */
    List<User> findByRoleName(String roleName);

    /**
     * Find all parking owners (approved and pending)
     * 
     * Custom JPQL query to get all users with OWNER role
     * 
     * @Query - Allows us to write custom queries
     *        JPQL (Java Persistence Query Language) uses entity names, not table
     *        names
     * 
     * @return List of owner users
     */
    @Query("SELECT u FROM User u WHERE u.role.name = 'OWNER'")
    List<User> findAllOwners();

    /**
     * Find pending owners (waiting for admin approval)
     * 
     * This query finds owners who are not yet approved
     * 
     * @return List of pending owner users
     */
    @Query("SELECT u FROM User u WHERE u.role.name = 'OWNER' AND u.isApproved = false")
    List<User> findPendingOwners();

    /**
     * Find approved owners
     * 
     * @return List of approved owner users
     */
    @Query("SELECT u FROM User u WHERE u.role.name = 'OWNER' AND u.isApproved = true")
    List<User> findApprovedOwners();

    /**
     * Count users by role
     * 
     * Used for admin dashboard statistics
     * 
     * @param roleName Role name
     * @return Count of users with that role
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
    Long countByRoleName(@Param("roleName") String roleName);
}
