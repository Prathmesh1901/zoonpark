package com.parkingbooking.controller;

import com.parkingbooking.dto.request.LoginRequest;
import com.parkingbooking.dto.request.RegisterRequest;
import com.parkingbooking.dto.response.AuthResponse;
import com.parkingbooking.entity.User;
import com.parkingbooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - REST API endpoints for authentication
 * 
 * Endpoints:
 * - POST /api/auth/register - Register new user
 * - POST /api/auth/login - Login user
 * - GET /api/auth/me - Get current user info
 * 
 * @RestController - Marks this as a REST controller
 * @RequestMapping - Base path for all endpoints in this controller
 * @CrossOrigin - Allows cross-origin requests (for frontend)
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * 
     * POST /api/auth/register
     * 
     * Request Body:
     * {
     * "email": "user@example.com",
     * "password": "password123",
     * "fullName": "John Doe",
     * "phone": "1234567890",
     * "role": "CUSTOMER"
     * }
     * 
     * Response:
     * {
     * "token": "eyJhbGciOiJIUzUxMiJ9...",
     * "type": "Bearer",
     * "id": 1,
     * "email": "user@example.com",
     * "fullName": "John Doe",
     * "role": "CUSTOMER",
     * "isApproved": true
     * }
     * 
     * @param request Registration data
     * @return AuthResponse with JWT token
     * 
     * @Valid - Validates the request body using annotations in RegisterRequest
     * @RequestBody - Extracts JSON from request body
     *              ResponseEntity - Allows us to set HTTP status code
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Login user
     * 
     * POST /api/auth/login
     * 
     * Request Body:
     * {
     * "email": "user@example.com",
     * "password": "password123"
     * }
     * 
     * Response: Same as register
     * 
     * @param request Login credentials
     * @return AuthResponse with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Get current user information
     * 
     * GET /api/auth/me
     * 
     * Headers:
     * Authorization: Bearer <token>
     * 
     * Response:
     * {
     * "id": 1,
     * "email": "user@example.com",
     * "fullName": "John Doe",
     * "phone": "1234567890",
     * "role": {
     * "id": 1,
     * "name": "CUSTOMER"
     * },
     * "isApproved": true
     * }
     * 
     * @return Current user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            User user = authService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
