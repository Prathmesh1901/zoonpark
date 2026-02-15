package com.parkingbooking.controller;

import com.parkingbooking.dto.response.MessageResponse;
import com.parkingbooking.entity.Booking;
import com.parkingbooking.entity.ParkingSpace;
import com.parkingbooking.entity.User;
import com.parkingbooking.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AdminController - REST API endpoints for admin operations
 * 
 * All endpoints require ADMIN role
 * 
 * Endpoints:
 * - GET /api/admin/users - Get all users
 * - GET /api/admin/owners - Get all owners
 * - GET /api/admin/owners/pending - Get pending owners
 * - PUT /api/admin/owners/{id}/approve - Approve owner
 * - PUT /api/admin/owners/{id}/block - Block owner
 * - GET /api/admin/bookings - Get all bookings
 * - GET /api/admin/parking-spaces - Get all parking spaces
 * - GET /api/admin/dashboard - Get dashboard statistics
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Get all users
     * 
     * GET /api/admin/users
     * 
     * Headers:
     * Authorization: Bearer <token> (ADMIN role required)
     * 
     * Response: List of all users
     * 
     * @return List of users
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all owners
     * 
     * GET /api/admin/owners
     * 
     * @return List of owner users
     */
    @GetMapping("/owners")
    public ResponseEntity<?> getAllOwners() {
        try {
            List<User> owners = adminService.getAllOwners();
            return ResponseEntity.ok(owners);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get pending owners (waiting for approval)
     * 
     * GET /api/admin/owners/pending
     * 
     * @return List of pending owner users
     */
    @GetMapping("/owners/pending")
    public ResponseEntity<?> getPendingOwners() {
        try {
            List<User> pendingOwners = adminService.getPendingOwners();
            return ResponseEntity.ok(pendingOwners);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Approve owner
     * 
     * PUT /api/admin/owners/1/approve
     * 
     * @param id User ID
     * @return Success message
     */
    @PutMapping("/owners/{id}/approve")
    public ResponseEntity<?> approveOwner(@PathVariable Long id) {
        try {
            String message = adminService.approveOwner(id, true);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Block owner
     * 
     * PUT /api/admin/owners/1/block
     * 
     * @param id User ID
     * @return Success message
     */
    @PutMapping("/owners/{id}/block")
    public ResponseEntity<?> blockOwner(@PathVariable Long id) {
        try {
            String message = adminService.approveOwner(id, false);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all bookings
     * 
     * GET /api/admin/bookings
     * 
     * @return List of all bookings
     */
    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = adminService.getAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get all parking spaces
     * 
     * GET /api/admin/parking-spaces
     * 
     * @return List of all parking spaces
     */
    @GetMapping("/parking-spaces")
    public ResponseEntity<?> getAllParkingSpaces() {
        try {
            List<ParkingSpace> parkingSpaces = adminService.getAllParkingSpaces();
            return ResponseEntity.ok(parkingSpaces);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get dashboard statistics
     * 
     * GET /api/admin/dashboard
     * 
     * Response:
     * {
     * "totalUsers": 100,
     * "totalCustomers": 80,
     * "totalOwners": 15,
     * "totalAdmins": 5,
     * "pendingOwners": 3,
     * "totalParkingSpaces": 50,
     * "activeParkingSpaces": 45,
     * "totalBookings": 500,
     * "confirmedBookings": 450,
     * "cancelledBookings": 30,
     * "completedBookings": 20
     * }
     * 
     * @return Dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Object> stats = adminService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get user by ID
     * 
     * GET /api/admin/users/1
     * 
     * @param id User ID
     * @return User details
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = adminService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
