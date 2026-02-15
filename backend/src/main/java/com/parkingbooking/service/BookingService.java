package com.parkingbooking.service;

import com.parkingbooking.dto.request.BookingRequest;
import com.parkingbooking.dto.response.BookingResponse;
import com.parkingbooking.entity.Booking;
import com.parkingbooking.entity.ParkingSpace;
import com.parkingbooking.entity.User;
import com.parkingbooking.repository.BookingRepository;
import com.parkingbooking.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BookingService - Handles all booking operations
 * 
 * This is the MOST CRITICAL service in the application!
 * 
 * Key Responsibilities:
 * 1. Check slot availability (without sensors!)
 * 2. Calculate booking price
 * 3. Create bookings
 * 4. Cancel bookings
 * 5. Retrieve booking history
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private AuthService authService;

    /**
     * ⭐ Check if slots are available for booking
     * 
     * This is the CORE availability logic!
     * 
     * Algorithm:
     * 1. Get total slots for the parking space
     * 2. Count overlapping confirmed bookings
     * 3. Calculate: available = total - overlapping
     * 4. Return true if available > 0
     * 
     * Example:
     * - Total slots: 50
     * - Overlapping bookings: 48
     * - Available: 50 - 48 = 2 slots
     * - Result: true (2 slots available)
     * 
     * @param parkingSpaceId Parking space ID
     * @param date           Booking date
     * @param startTime      Start time
     * @param endTime        End time
     * @return true if slots available, false otherwise
     */
    public boolean checkAvailability(Long parkingSpaceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Get parking space
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(parkingSpaceId)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Count overlapping bookings
        Long overlappingBookings = bookingRepository.countOverlappingBookings(
                parkingSpaceId, date, startTime, endTime);

        // Calculate available slots
        int availableSlots = parkingSpace.getTotalSlots() - overlappingBookings.intValue();

        // Return true if at least one slot is available
        return availableSlots > 0;
    }

    /**
     * Get number of available slots
     * 
     * @param parkingSpaceId Parking space ID
     * @param date           Booking date
     * @param startTime      Start time
     * @param endTime        End time
     * @return Number of available slots
     */
    public int getAvailableSlots(Long parkingSpaceId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(parkingSpaceId)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        Long overlappingBookings = bookingRepository.countOverlappingBookings(
                parkingSpaceId, date, startTime, endTime);

        return parkingSpace.getTotalSlots() - overlappingBookings.intValue();
    }

    /**
     * Calculate booking price
     * 
     * Formula: (hours) * (price per hour)
     * 
     * Example:
     * - Start: 10:00
     * - End: 14:00
     * - Duration: 4 hours
     * - Price per hour: ₹50
     * - Total: 4 * 50 = ₹200
     * 
     * @param parkingSpaceId Parking space ID
     * @param startTime      Start time
     * @param endTime        End time
     * @return Total price
     */
    public BigDecimal calculatePrice(Long parkingSpaceId, LocalTime startTime, LocalTime endTime) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(parkingSpaceId)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Calculate duration in hours
        Duration duration = Duration.between(startTime, endTime);
        long hours = duration.toHours();

        // If duration is less than 1 hour, charge for 1 hour minimum
        if (hours < 1) {
            hours = 1;
        }

        // Calculate total price
        return parkingSpace.getPricePerHour().multiply(BigDecimal.valueOf(hours));
    }

    /**
     * Create a new booking
     * 
     * Process:
     * 1. Validate parking space exists and is active
     * 2. Validate time range
     * 3. Check availability
     * 4. Calculate price
     * 5. Generate unique booking ID
     * 6. Create and save booking
     * 
     * @Transactional ensures all steps succeed or all fail (atomicity)
     * 
     * @param request Booking request
     * @return BookingResponse
     * @throws RuntimeException if validation fails or no slots available
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Step 1: Get current user
        User user = authService.getCurrentUser();

        // Step 2: Get parking space
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(request.getParkingSpaceId())
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Step 3: Validate parking space is active
        if (!parkingSpace.getIsActive()) {
            throw new RuntimeException("Parking space is not active");
        }

        // Step 4: Validate time range
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        // Step 5: Validate operating hours
        if (request.getStartTime().isBefore(parkingSpace.getOpeningTime()) ||
                request.getEndTime().isAfter(parkingSpace.getClosingTime())) {
            throw new RuntimeException("Booking time must be within operating hours: " +
                    parkingSpace.getOpeningTime() + " - " + parkingSpace.getClosingTime());
        }

        // Step 6: Check availability
        if (!checkAvailability(request.getParkingSpaceId(), request.getBookingDate(),
                request.getStartTime(), request.getEndTime())) {
            throw new RuntimeException("No slots available for the selected time");
        }

        // Step 7: Calculate price
        BigDecimal totalPrice = calculatePrice(request.getParkingSpaceId(),
                request.getStartTime(), request.getEndTime());

        // Step 8: Generate unique booking ID
        String bookingId = generateBookingId();

        // Step 9: Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setParkingSpace(parkingSpace);
        booking.setBookingId(bookingId);
        booking.setBookingDate(request.getBookingDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");

        // Step 10: Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Step 11: Return response
        return convertToResponse(savedBooking);
    }

    /**
     * Generate unique booking ID
     * 
     * Format: BK + YYYYMMDD + sequence
     * Example: BK20260129001
     * 
     * @return Unique booking ID
     */
    private String generateBookingId() {
        String datePrefix = "BK" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = bookingRepository.countByBookingIdPrefix(datePrefix);
        return String.format("%s%03d", datePrefix, count + 1);
    }

    /**
     * Cancel a booking
     * 
     * Only the user who created the booking can cancel it
     * 
     * @param bookingId Booking ID
     * @return Success message
     * @throws RuntimeException if booking not found or user not authorized
     */
    @Transactional
    public String cancelBooking(String bookingId) {
        User currentUser = authService.getCurrentUser();

        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user owns this booking
        if (!booking.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        // Check if booking is already cancelled
        if (booking.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Update status to cancelled
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return "Booking cancelled successfully";
    }

    /**
     * Get user's booking history
     * 
     * @return List of user's bookings
     */
    public List<BookingResponse> getUserBookings() {
        User currentUser = authService.getCurrentUser();
        List<Booking> bookings = bookingRepository.findByUserId(currentUser.getId());
        return bookings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get booking by ID
     * 
     * @param bookingId Booking ID
     * @return BookingResponse
     * @throws RuntimeException if booking not found
     */
    public BookingResponse getBookingById(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return convertToResponse(booking);
    }

    /**
     * Convert Booking entity to BookingResponse DTO
     * 
     * @param booking Booking entity
     * @return BookingResponse DTO
     */
    private BookingResponse convertToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setBookingId(booking.getBookingId());
        response.setUserName(booking.getUser().getFullName());
        response.setUserEmail(booking.getUser().getEmail());
        response.setParkingSpaceName(booking.getParkingSpace().getName());
        response.setParkingLocation(booking.getParkingSpace().getCity() + ", " + booking.getParkingSpace().getArea());
        response.setBookingDate(booking.getBookingDate());
        response.setStartTime(booking.getStartTime());
        response.setEndTime(booking.getEndTime());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt().toString());
        return response;
    }
}
