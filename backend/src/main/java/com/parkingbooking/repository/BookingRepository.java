package com.parkingbooking.repository;

import com.parkingbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * BookingRepository - Data access layer for Booking entity
 * 
 * This is the MOST IMPORTANT repository for the booking system
 * Contains the critical query for checking slot availability
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find booking by unique booking ID
     * 
     * Used when users want to view their booking details
     * 
     * @param bookingId Unique booking identifier (e.g., "BK20260129001")
     * @return Optional containing booking if found
     */
    Optional<Booking> findByBookingId(String bookingId);

    /**
     * Find all bookings for a specific user
     * 
     * Used in user dashboard to show booking history
     * Orders by creation date (newest first)
     * 
     * @param userId User's ID
     * @return List of bookings ordered by date
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.createdAt DESC")
    List<Booking> findByUserId(@Param("userId") Long userId);

    /**
     * Find all bookings for a specific parking space
     * 
     * Used by owners to see who booked their parking
     * 
     * @param parkingSpaceId Parking space ID
     * @return List of bookings for that parking space
     */
    List<Booking> findByParkingSpaceId(Long parkingSpaceId);

    /**
     * Find bookings by status
     * 
     * @param status Booking status (CONFIRMED, CANCELLED, COMPLETED)
     * @return List of bookings with that status
     */
    List<Booking> findByStatus(String status);

    /**
     * ⭐ CRITICAL QUERY - Count overlapping bookings
     * 
     * This is the CORE of the availability checking logic!
     * 
     * How it works:
     * 1. We want to book parking from startTime to endTime on a specific date
     * 2. We need to check if any CONFIRMED bookings overlap with this time
     * 3. Two time ranges overlap if:
     * - New start < Existing end AND
     * - New end > Existing start
     * 
     * Example:
     * Existing booking: 10:00 - 14:00
     * New booking: 12:00 - 16:00
     * Check: 12:00 < 14:00 (true) AND 16:00 > 10:00 (true) = OVERLAP!
     * 
     * Visual representation:
     * Existing: |----10:00----14:00----|
     * New: |----12:00----16:00----|
     * ^^^^^ Overlap!
     * 
     * @param parkingSpaceId The parking space we want to book
     * @param bookingDate    The date we want to book
     * @param startTime      Our desired start time
     * @param endTime        Our desired end time
     * @return Count of overlapping CONFIRMED bookings
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.parkingSpace.id = :parkingSpaceId " +
            "AND b.bookingDate = :bookingDate " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime " +
            "AND b.status = 'CONFIRMED'")
    Long countOverlappingBookings(
            @Param("parkingSpaceId") Long parkingSpaceId,
            @Param("bookingDate") LocalDate bookingDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Find overlapping bookings (detailed version)
     * 
     * Same logic as above, but returns the actual bookings
     * Useful for debugging or showing which slots are taken
     * 
     * @param parkingSpaceId Parking space ID
     * @param bookingDate    Booking date
     * @param startTime      Start time
     * @param endTime        End time
     * @return List of overlapping bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.parkingSpace.id = :parkingSpaceId " +
            "AND b.bookingDate = :bookingDate " +
            "AND b.startTime < :endTime " +
            "AND b.endTime > :startTime " +
            "AND b.status = 'CONFIRMED'")
    List<Booking> findOverlappingBookings(
            @Param("parkingSpaceId") Long parkingSpaceId,
            @Param("bookingDate") LocalDate bookingDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    /**
     * Find bookings for a specific date and parking space
     * 
     * @param parkingSpaceId Parking space ID
     * @param bookingDate    Booking date
     * @return List of all bookings for that date
     */
    @Query("SELECT b FROM Booking b WHERE b.parkingSpace.id = :parkingSpaceId " +
            "AND b.bookingDate = :bookingDate " +
            "AND b.status = 'CONFIRMED' " +
            "ORDER BY b.startTime")
    List<Booking> findByParkingSpaceAndDate(
            @Param("parkingSpaceId") Long parkingSpaceId,
            @Param("bookingDate") LocalDate bookingDate);

    /**
     * Count total bookings
     * 
     * Used for admin dashboard statistics
     * 
     * @return Total number of bookings
     */
    @Query("SELECT COUNT(b) FROM Booking b")
    Long countAllBookings();

    /**
     * Count bookings by status
     * 
     * @param status Booking status
     * @return Count of bookings with that status
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    Long countByStatus(@Param("status") String status);

    /**
     * Generate next booking ID sequence
     * 
     * Finds the highest booking ID for today and increments it
     * Format: BK + YYYYMMDD + sequence (e.g., BK20260129001)
     * 
     * @param prefix Booking ID prefix (e.g., "BK20260129")
     * @return Count of bookings with that prefix (used for sequence)
     */
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingId LIKE CONCAT(:prefix, '%')")
    Long countByBookingIdPrefix(@Param("prefix") String prefix);
}
