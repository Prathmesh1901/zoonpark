package com.parkingbooking.repository;

import com.parkingbooking.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ParkingSpaceRepository - Data access layer for ParkingSpace entity
 * 
 * Provides methods to search and filter parking spaces
 * Includes location-based queries for the search functionality
 */
@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    /**
     * Find all parking spaces by city
     * 
     * Generated query: SELECT * FROM parking_spaces WHERE city = ?
     * 
     * @param city City name
     * @return List of parking spaces in that city
     */
    List<ParkingSpace> findByCity(String city);

    /**
     * Find parking spaces by city and area
     * 
     * Generated query:
     * SELECT * FROM parking_spaces WHERE city = ? AND area = ?
     * 
     * @param city City name
     * @param area Area/locality name
     * @return List of parking spaces matching both criteria
     */
    List<ParkingSpace> findByCityAndArea(String city, String area);

    /**
     * Find all active parking spaces
     * 
     * Only returns parking spaces that are currently active
     * 
     * @return List of active parking spaces
     */
    List<ParkingSpace> findByIsActiveTrue();

    /**
     * Find active parking spaces by city
     * 
     * Combines city filter with active status
     * 
     * @param city City name
     * @return List of active parking spaces in the city
     */
    List<ParkingSpace> findByCityAndIsActiveTrue(String city);

    /**
     * Find active parking spaces by city and area
     * 
     * Most specific search - used in the main search functionality
     * 
     * @param city City name
     * @param area Area name
     * @return List of active parking spaces matching location
     */
    List<ParkingSpace> findByCityAndAreaAndIsActiveTrue(String city, String area);

    /**
     * Find all parking spaces owned by a specific user
     * 
     * Used in owner dashboard to show their parking spaces
     * 
     * @param ownerId Owner's user ID
     * @return List of parking spaces owned by this user
     */
    List<ParkingSpace> findByOwnerId(Long ownerId);

    /**
     * Search parking spaces by city (case-insensitive, partial match)
     * 
     * Custom query using LIKE for flexible searching
     * Example: "mum" will match "Mumbai"
     * 
     * LOWER() - Converts to lowercase for case-insensitive search
     * CONCAT('%', :city, '%') - Adds wildcards for partial matching
     * 
     * @param city City name or partial city name
     * @return List of matching parking spaces
     */
    @Query("SELECT p FROM ParkingSpace p WHERE LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')) AND p.isActive = true")
    List<ParkingSpace> searchByCity(@Param("city") String city);

    /**
     * Search parking spaces by city and area (case-insensitive)
     * 
     * @param city City name or partial
     * @param area Area name or partial
     * @return List of matching parking spaces
     */
    @Query("SELECT p FROM ParkingSpace p WHERE LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
            "AND LOWER(p.area) LIKE LOWER(CONCAT('%', :area, '%')) AND p.isActive = true")
    List<ParkingSpace> searchByCityAndArea(@Param("city") String city, @Param("area") String area);

    /**
     * Count total parking spaces
     * 
     * Used for admin dashboard
     * 
     * @return Total count of parking spaces
     */
    @Query("SELECT COUNT(p) FROM ParkingSpace p")
    Long countAllParkingSpaces();

    /**
     * Count active parking spaces
     * 
     * @return Count of active parking spaces
     */
    @Query("SELECT COUNT(p) FROM ParkingSpace p WHERE p.isActive = true")
    Long countActiveParkingSpaces();
}
