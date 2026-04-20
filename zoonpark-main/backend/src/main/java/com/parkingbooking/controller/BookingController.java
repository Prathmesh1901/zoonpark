package com.parkingbooking.controller;

import com.parkingbooking.dto.request.BookingRequest;
import com.parkingbooking.dto.response.BookingResponse;
import com.parkingbooking.dto.response.MessageResponse;
import com.parkingbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BookingController - REST API endpoints for booking operations
 * 
 * Endpoints:
 * - POST /api/bookings/check-availability - Check if slots available
 * - GET /api/bookings/calculate-price - Calculate booking price
 * - POST /api/bookings - Create booking
 * - GET /api/bookings/{bookingId} - Get booking details
 * - PUT /api/bookings/{bookingId}/cancel - Cancel booking
 * - GET /api/bookings/my-bookings - Get user's bookings
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Check availability
     * 
     * GET
     * /api/bookings/check-availability?parkingSpaceId=1&date=2026-01-30&startTime=10:00&endTime=14:00
     * 
     * Response:
     * {
     * "available": true,
     * "availableSlots": 5
     * }
     * 
     * @param parkingSpaceId Parking space ID
     * @param date           Booking date
     * @param startTime      Start time
     * @param endTime        End time
     * @return Availability status
     * 
     * @RequestParam - Extracts query parameters from URL
     * @DateTimeFormat - Specifies date/time format
     */
    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam Long parkingSpaceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        try {
            boolean available = bookingService.checkAvailability(parkingSpaceId, date, startTime, endTime);
            int availableSlots = bookingService.getAvailableSlots(parkingSpaceId, date, startTime, endTime);

            Map<String, Object> response = new HashMap<>();
            response.put("available", available);
            response.put("availableSlots", availableSlots);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Calculate booking price
     * 
     * GET
     * /api/bookings/calculate-price?parkingSpaceId=1&startTime=10:00&endTime=14:00
     * 
     * Response:
     * {
     * "totalPrice": 200.00,
     * "hours": 4,
     * "pricePerHour": 50.00
     * }
     * 
     * @param parkingSpaceId Parking space ID
     * @param startTime      Start time
     * @param endTime        End time
     * @return Price calculation
     */
    @GetMapping("/calculate-price")
    public ResponseEntity<?> calculatePrice(
            @RequestParam Long parkingSpaceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        try {
            BigDecimal totalPrice = bookingService.calculatePrice(parkingSpaceId, startTime, endTime);

            Map<String, Object> response = new HashMap<>();
            response.put("totalPrice", totalPrice);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Create booking
     * 
     * POST /api/bookings
     * 
     * Headers:
     * Authorization: Bearer <token>
     * 
     * Request Body:
     * {
     * "parkingSpaceId": 1,
     * "bookingDate": "2026-01-30",
     * "startTime": "10:00",
     * "endTime": "14:00"
     * }
     * 
     * Response: BookingResponse
     * 
     * @param request Booking data
     * @return Created booking
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get booking by ID
     * 
     * GET /api/bookings/{bookingId}
     * 
     * @param bookingId Booking ID (e.g., BK20260129001)
     * @return Booking details
     * 
     * @PathVariable - Extracts value from URL path
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBooking(@PathVariable String bookingId) {
        try {
            BookingResponse response = bookingService.getBookingById(bookingId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Cancel booking
     * 
     * PUT /api/bookings/{bookingId}/cancel
     * 
     * Headers:
     * Authorization: Bearer <token>
     * 
     * @param bookingId Booking ID
     * @return Success message
     */
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId) {
        try {
            String message = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get user's bookings
     * 
     * GET /api/bookings/my-bookings
     * 
     * Headers:
     * Authorization: Bearer <token>
     * 
     * @return List of user's bookings
     */
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        try {
            List<BookingResponse> bookings = bookingService.getUserBookings();
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
