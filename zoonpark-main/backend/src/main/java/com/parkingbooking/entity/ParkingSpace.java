package com.parkingbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * ParkingSpace Entity - Represents a parking location
 * 
 * This entity stores information about parking spaces added by OWNER users
 * 
 * Key Features:
 * - Each parking space has multiple slots (e.g., 50 slots)
 * - Availability is calculated dynamically based on bookings
 * - Operating hours define when the parking is open
 * - Price is charged per hour
 * 
 * Example:
 * - Name: "City Center Parking"
 * - Location: Mumbai, Andheri
 * - Total Slots: 50
 * - Operating Hours: 06:00 - 23:00
 * - Price: ₹50/hour
 */
@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the owner who created this parking space
     * 
     * @ManyToOne - Many parking spaces can belong to one owner
     * @JoinColumn - Creates foreign key column 'owner_id'
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User owner;

    /**
     * Parking space name (e.g., "City Center Parking")
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * City where parking is located
     */
    @Column(nullable = false, length = 50)
    private String city;

    /**
     * Area/locality within the city
     */
    @Column(nullable = false, length = 100)
    private String area;

    /**
     * Full address of the parking location
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * Total number of parking slots available
     * 
     * Example: If totalSlots = 50, then maximum 50 cars can park
     * at the same time (if no overlapping bookings)
     */
    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    /**
     * Price per hour for parking
     * 
     * BigDecimal is used for precise monetary calculations
     * precision = 10, scale = 2 means: 10 total digits, 2 after decimal
     * Example: 9999999.99
     */
    @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    /**
     * Parking opening time (e.g., 06:00)
     */
    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    /**
     * Parking closing time (e.g., 23:00)
     */
    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    /**
     * Whether this parking space is currently active
     * - true: Available for booking
     * - false: Temporarily disabled by owner
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * When this parking space was created
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
    public ParkingSpace() {
    }

    public ParkingSpace(Long id, User owner, String name, String city, String area, String address,
            Integer totalSlots, BigDecimal pricePerHour, LocalTime openingTime,
            LocalTime closingTime, Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.city = city;
        this.area = area;
        this.address = address;
        this.totalSlots = totalSlots;
        this.pricePerHour = pricePerHour;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
