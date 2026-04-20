package com.parkingbooking.service;

import com.parkingbooking.entity.Booking;
import com.parkingbooking.entity.ParkingSpace;
import com.parkingbooking.entity.User;
import com.parkingbooking.repository.BookingRepository;
import com.parkingbooking.repository.ParkingSpaceRepository;
import com.parkingbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminService - Handles admin operations
 * 
 * Responsibilities:
 * 1. Approve/block parking owners
 * 2. View all users
 * 3. View all bookings
 * 4. Get dashboard statistics
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    /**
     * Get all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get all parking owners
     * 
     * @return List of owner users
     */
    public List<User> getAllOwners() {
        return userRepository.findAllOwners();
    }

    /**
     * Get pending owners (waiting for approval)
     * 
     * @return List of pending owner users
     */
    public List<User> getPendingOwners() {
        return userRepository.findPendingOwners();
    }

    /**
     * Approve or block an owner
     * 
     * Admin can:
     * - Approve pending owners (isApproved = true)
     * - Block approved owners (isApproved = false)
     * 
     * @param userId  User ID
     * @param approve true to approve, false to block
     * @return Success message
     * @throws RuntimeException if user not found or not an owner
     */
    @Transactional
    public String approveOwner(Long userId, boolean approve) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().getName().equals("OWNER")) {
            throw new RuntimeException("User is not an owner");
        }

        user.setIsApproved(approve);
        userRepository.save(user);

        return approve ? "Owner approved successfully" : "Owner blocked successfully";
    }

    /**
     * Get all bookings
     * 
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get all parking spaces
     * 
     * @return List of all parking spaces
     */
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }

    /**
     * Get dashboard statistics
     * 
     * Returns:
     * - Total users
     * - Total customers
     * - Total owners
     * - Total admins
     * - Total parking spaces
     * - Total bookings
     * - Confirmed bookings
     * - Cancelled bookings
     * - Pending owners
     * 
     * @return Map of statistics
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // User statistics
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCustomers", userRepository.countByRoleName("CUSTOMER"));
        stats.put("totalOwners", userRepository.countByRoleName("OWNER"));
        stats.put("totalAdmins", userRepository.countByRoleName("ADMIN"));
        stats.put("pendingOwners", userRepository.findPendingOwners().size());

        // Parking space statistics
        stats.put("totalParkingSpaces", parkingSpaceRepository.count());
        stats.put("activeParkingSpaces", parkingSpaceRepository.countActiveParkingSpaces());

        // Booking statistics
        stats.put("totalBookings", bookingRepository.count());
        stats.put("confirmedBookings", bookingRepository.countByStatus("CONFIRMED"));
        stats.put("cancelledBookings", bookingRepository.countByStatus("CANCELLED"));
        stats.put("completedBookings", bookingRepository.countByStatus("COMPLETED"));

        return stats;
    }

    /**
     * Get user by ID
     * 
     * @param userId User ID
     * @return User
     * @throws RuntimeException if user not found
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
