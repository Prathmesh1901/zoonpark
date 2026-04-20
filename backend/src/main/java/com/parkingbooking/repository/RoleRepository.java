package com.parkingbooking.repository;

import com.parkingbooking.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RoleRepository - Data access layer for Role entity
 * 
 * Spring Data JPA automatically implements this interface
 * No need to write SQL queries for basic CRUD operations!
 * 
 * JpaRepository provides methods like:
 * - save() - Create or update
 * - findById() - Find by primary key
 * - findAll() - Get all records
 * - deleteById() - Delete by primary key
 * - count() - Count total records
 * 
 * We can also define custom query methods using method naming conventions
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find a role by its name
     * 
     * Spring Data JPA automatically generates the query:
     * SELECT * FROM roles WHERE name = ?
     * 
     * @param name Role name (e.g., "CUSTOMER", "OWNER", "ADMIN")
     * @return Optional containing the role if found, empty otherwise
     * 
     *         Why Optional?
     *         - Avoids null pointer exceptions
     *         - Forces us to handle the case when role is not found
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role exists by name
     * 
     * Generated query: SELECT COUNT(*) > 0 FROM roles WHERE name = ?
     * 
     * @param name Role name
     * @return true if role exists, false otherwise
     */
    boolean existsByName(String name);
}
