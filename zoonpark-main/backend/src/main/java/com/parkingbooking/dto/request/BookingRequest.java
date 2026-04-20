package com.parkingbooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * BookingRequest DTO - Data for creating a booking
 * 
 * User provides:
 * - Which parking space to book
 * - What date
 * - Start and end time
 * 
 * The system will:
 * - Check availability
 * - Calculate price
 * - Create booking if slots available
 */
public class BookingRequest {

    @NotNull(message = "Parking space ID is required")
    private Long parkingSpaceId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in the future")
    private LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    // Constructors
    public BookingRequest() {
    }

    public BookingRequest(Long parkingSpaceId, LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        this.parkingSpaceId = parkingSpaceId;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
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
}
