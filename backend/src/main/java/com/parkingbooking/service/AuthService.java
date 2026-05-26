package com.parkingbooking.service;

import com.parkingbooking.dto.request.LoginRequest;
import com.parkingbooking.dto.request.RegisterRequest;
import com.parkingbooking.dto.response.AuthResponse;
import com.parkingbooking.entity.Role;
import com.parkingbooking.entity.User;
import com.parkingbooking.repository.RoleRepository;
import com.parkingbooking.repository.UserRepository;
import com.parkingbooking.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService - Handles user authentication and registration
 * 
 * This service contains the business logic for:
 * - User registration (sign up)
 * - User login (authentication)
 * - JWT token generation
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Register a new user
     * 
     * Process:
     * 1. Check if email already exists
     * 2. Find the requested role
     * 3. Encode the password (BCrypt)
     * 4. Create user entity
     * 5. Save to database
     * 6. Generate JWT token
     * 7. Return auth response
     * 
     * @param request Registration data
     * @return AuthResponse with token and user info
     * @throws RuntimeException if email exists or role not found
     */
    public AuthResponse register(RegisterRequest request) {
        // Step 1: Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Step 2: Find role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

        // Step 3: Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(role);

        // Auto-approve all users for development/testing
        // In production, you may want to keep OWNER approval requirement
        user.setIsApproved(true);

        // Step 4: Save user to database
        User savedUser = userRepository.save(user);

        // Step 5: Generate JWT token
        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());

        // Step 6: Return response
        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().getName(),
                savedUser.getIsApproved());
    }

    /**
     * Login user
     * 
     * Process:
     * 1. Authenticate with email and password
     * 2. If successful, generate JWT token
     * 3. Return auth response
     * 
     * @param request Login credentials
     * @return AuthResponse with token and user info
     * @throws RuntimeException if authentication fails
     */
    public AuthResponse login(LoginRequest request) {
        // Step 1: Authenticate user
        // This will throw exception if credentials are wrong
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Step 2: Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Step 3: Generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        // Step 4: Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 5: Return response
        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().getName(),
                user.getIsApproved());
    }

    /**
     * Get currently authenticated user
     * 
     * @return Current user
     * @throws RuntimeException if user not found
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthResponse updateCurrentUserRole(String roleName) {
        if (!"CUSTOMER".equals(roleName) && !"OWNER".equals(roleName)) {
            throw new RuntimeException("Invalid account type");
        }

        User user = getCurrentUser();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.setRole(role);
        user.setIsApproved(true);
        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());
        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole().getName(),
                savedUser.getIsApproved());
    }
}
