package com.parkingbooking.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * ParkingSpaceRequest DTO - Data for creating/updating parking space
 * 
 * Used by OWNER users to add or modify their parking spaces
 */
public class ParkingSpaceRequest {

    @NotBlank(message = "Parking name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City name cannot exceed 50 characters")
    private String city;

    @NotBlank(message = "Area is required")
    @Size(max = 100, message = "Area name cannot exceed 100 characters")
    private String area;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Total slots is required")
    @Min(value = 1, message = "Must have at least 1 slot")
    @Max(value = 1000, message = "Cannot exceed 1000 slots")
    private Integer totalSlots;

    @NotNull(message = "Price per hour is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "10000.00", message = "Price cannot exceed 10000")
    private BigDecimal pricePerHour;

    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;

    // Constructors
    public ParkingSpaceRequest() {
    }

    public ParkingSpaceRequest(String name, String city, String area, String address,
            Integer totalSlots, BigDecimal pricePerHour,
            LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.city = city;
        this.area = area;
        this.address = address;
        this.totalSlots = totalSlots;
        this.pricePerHour = pricePerHour;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    // Getters and Setters
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
}
