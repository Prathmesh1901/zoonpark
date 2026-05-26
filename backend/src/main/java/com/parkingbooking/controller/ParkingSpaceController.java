package com.parkingbooking.controller;

import com.parkingbooking.dto.request.ParkingSpaceRequest;
import com.parkingbooking.dto.response.MessageResponse;
import com.parkingbooking.entity.ParkingSpace;
import com.parkingbooking.service.ParkingSpaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ParkingSpaceController - REST API endpoints for parking space management
 * 
 * Endpoints:
 * - POST /api/parking-spaces - Create parking space (OWNER only)
 * - GET /api/parking-spaces - Search parking spaces
 * - GET /api/parking-spaces/{id} - Get parking space details
 * - PUT /api/parking-spaces/{id} - Update parking space (OWNER only)
 * - DELETE /api/parking-spaces/{id} - Delete parking space (OWNER only)
 * - GET /api/parking-spaces/my-spaces - Get owner's parking spaces
 */
@RestController
@RequestMapping("/parking-spaces")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    /**
     * Create parking space
     * 
     * POST /api/parking-spaces
     * 
     * Headers:
     * Authorization: Bearer <token> (OWNER role required)
     * 
     * Request Body:
     * {
     * "name": "City Center Parking",
     * "city": "Mumbai",
     * "area": "Andheri",
     * "address": "123 Main Street, Andheri, Mumbai",
     * "totalSlots": 50,
     * "pricePerHour": 50.00,
     * "openingTime": "06:00",
     * "closingTime": "23:00"
     * }
     * 
     * Response: Created ParkingSpace
     * 
     * @param request Parking space data
     * @return Created parking space
     */
    @PostMapping
    public ResponseEntity<?> createParkingSpace(@Valid @RequestBody ParkingSpaceRequest request) {
        try {
            ParkingSpace parkingSpace = parkingSpaceService.createParkingSpace(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Search parking spaces
     * 
     * GET /api/parking-spaces?city=Mumbai
     * GET /api/parking-spaces?city=Mumbai&area=Andheri
     * GET /api/parking-spaces (all active parking spaces)
     * 
     * @param city Optional city filter
     * @param area Optional area filter
     * @return List of parking spaces
     * 
     * @RequestParam(required = false) - Makes parameter optional
     */
    @GetMapping
    public ResponseEntity<?> searchParkingSpaces(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area) {
        System.out.println("🔍 Search Request Received: city=" + city + ", area=" + area);
        try {
            List<ParkingSpace> parkingSpaces;

            if (city != null && area != null) {
                parkingSpaces = parkingSpaceService.searchByCityAndArea(city, area);
            } else if (city != null) {
                parkingSpaces = parkingSpaceService.searchByCity(city);
            } else {
                parkingSpaces = parkingSpaceService.getAllActiveParkingSpaces();
            }

            return ResponseEntity.ok(parkingSpaces);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get parking space by ID
     * 
     * GET /api/parking-spaces/1
     * 
     * @param id Parking space ID
     * @return Parking space details
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getParkingSpace(@PathVariable Long id) {
        try {
            ParkingSpace parkingSpace = parkingSpaceService.getParkingSpaceById(id);
            return ResponseEntity.ok(parkingSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Update parking space
     * 
     * PUT /api/parking-spaces/1
     * 
     * Headers:
     * Authorization: Bearer <token> (Must be the owner)
     * 
     * Request Body: Same as create
     * 
     * @param id      Parking space ID
     * @param request Updated data
     * @return Updated parking space
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateParkingSpace(
            @PathVariable Long id,
            @Valid @RequestBody ParkingSpaceRequest request) {
        try {
            ParkingSpace parkingSpace = parkingSpaceService.updateParkingSpace(id, request);
            return ResponseEntity.ok(parkingSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Delete parking space
     * 
     * DELETE /api/parking-spaces/1
     * 
     * Headers:
     * Authorization: Bearer <token> (Must be the owner)
     * 
     * @param id Parking space ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParkingSpace(@PathVariable Long id) {
        try {
            String message = parkingSpaceService.deleteParkingSpace(id);
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Get owner's parking spaces
     * 
     * GET /api/parking-spaces/my-spaces
     * 
     * Headers:
     * Authorization: Bearer <token> (OWNER role required)
     * 
     * @return List of owner's parking spaces
     */
    @GetMapping("/my-spaces")
    public ResponseEntity<?> getMyParkingSpaces() {
        try {
            List<ParkingSpace> parkingSpaces = parkingSpaceService.getMyParkingSpaces();
            return ResponseEntity.ok(parkingSpaces);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
