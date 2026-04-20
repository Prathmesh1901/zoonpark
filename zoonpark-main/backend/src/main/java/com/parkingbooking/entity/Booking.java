package com.parkingbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Booking Entity - Represents a parking space reservation
 * 
 * This is the core entity for the booking system
 * 
 * How it works:
 * 1. User selects parking space, date, and time range
 * 2. System checks if slots are available (total_slots - overlapping_bookings)
 * 3. If available, booking is created with CONFIRMED status
 * 4. Price is calculated: hours * pricePerHour
 * 
 * Example Booking:
 * - User: john@example.com
 * - Parking: City Center Parking
 * - Date: 2026-01-30
 * - Time: 10:00 - 14:00 (4 hours)
 * - Price: 4 * ₹50 = ₹200
 * - Booking ID: BK20260129001
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the user who made this booking
     * 
     * @ManyToOne - Many bookings can be made by one user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Reference to the parking space being booked
     * 
     * @ManyToOne - Many bookings can be for one parking space
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    /**
     * Unique booking identifier (e.g., BK20260129001)
     * 
     * This is shown to users for reference
     * Format: BK + YYYYMMDD + sequence number
     */
    @Column(name = "booking_id", nullable = false, unique = true, length = 20)
    private String bookingId;

    /**
     * Date of the booking
     * 
     * Example: 2026-01-30
     */
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    /**
     * Start time of the booking
     * 
     * Example: 10:00
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    /**
     * End time of the booking
     * 
     * Example: 14:00
     */
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * Total price for this booking
     * 
     * Calculated as: (hours) * (pricePerHour)
     * Example: 4 hours * ₹50 = ₹200
     */
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Booking status
     * 
     * Possible values:
     * - CONFIRMED: Booking is active
     * - CANCELLED: User cancelled the booking
     * - COMPLETED: Booking time has passed
     */
    @Column(nullable = false, length = 20)
    private String status = "CONFIRMED";

    /**
     * When this booking was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically set creation timestamp
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Booking() {
    }

    public Booking(Long id, User user, ParkingSpace parkingSpace, String bookingId,
            LocalDate bookingDate, LocalTime startTime, LocalTime endTime,
            BigDecimal totalPrice, String status, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.parkingSpace = parkingSpace;
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
