package com.parkingbooking.service;

import com.parkingbooking.dto.request.ParkingSpaceRequest;
import com.parkingbooking.entity.ParkingSpace;
import com.parkingbooking.entity.User;
import com.parkingbooking.repository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ParkingSpaceService - Handles parking space management
 * 
 * Responsibilities:
 * 1. Create parking spaces (OWNER only)
 * 2. Update parking spaces
 * 3. Delete parking spaces
 * 4. Search parking spaces by location
 * 5. Get owner's parking spaces
 */
@Service
public class ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private AuthService authService;

    /**
     * Create a new parking space
     * 
     * Only OWNER users can create parking spaces
     * Owner must be approved by admin first
     * 
     * @param request Parking space data
     * @return Created parking space
     * @throws RuntimeException if user is not approved owner
     */
    @Transactional
    public ParkingSpace createParkingSpace(ParkingSpaceRequest request) {
        // Get current user
        User currentUser = authService.getCurrentUser();

        // Validate user is an approved owner
        if (!currentUser.getRole().getName().equals("OWNER")) {
            throw new RuntimeException("Only owners can create parking spaces");
        }

        if (!currentUser.getIsApproved()) {
            throw new RuntimeException("Your account is pending admin approval");
        }

        // Validate operating hours
        if (request.getClosingTime().isBefore(request.getOpeningTime())) {
            throw new RuntimeException("Closing time must be after opening time");
        }

        // Create parking space
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setOwner(currentUser);
        parkingSpace.setName(request.getName());
        parkingSpace.setCity(request.getCity());
        parkingSpace.setArea(request.getArea());
        parkingSpace.setAddress(request.getAddress());
        parkingSpace.setTotalSlots(request.getTotalSlots());
        parkingSpace.setPricePerHour(request.getPricePerHour());
        parkingSpace.setOpeningTime(request.getOpeningTime());
        parkingSpace.setClosingTime(request.getClosingTime());
        parkingSpace.setIsActive(true);

        return parkingSpaceRepository.save(parkingSpace);
    }

    /**
     * Update parking space
     * 
     * Only the owner who created it can update
     * 
     * @param id      Parking space ID
     * @param request Updated data
     * @return Updated parking space
     * @throws RuntimeException if not found or user not authorized
     */
    @Transactional
    public ParkingSpace updateParkingSpace(Long id, ParkingSpaceRequest request) {
        User currentUser = authService.getCurrentUser();

        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Check ownership
        if (!parkingSpace.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this parking space");
        }

        // Validate operating hours
        if (request.getClosingTime().isBefore(request.getOpeningTime())) {
            throw new RuntimeException("Closing time must be after opening time");
        }

        // Update fields
        parkingSpace.setName(request.getName());
        parkingSpace.setCity(request.getCity());
        parkingSpace.setArea(request.getArea());
        parkingSpace.setAddress(request.getAddress());
        parkingSpace.setTotalSlots(request.getTotalSlots());
        parkingSpace.setPricePerHour(request.getPricePerHour());
        parkingSpace.setOpeningTime(request.getOpeningTime());
        parkingSpace.setClosingTime(request.getClosingTime());

        return parkingSpaceRepository.save(parkingSpace);
    }

    /**
     * Delete (deactivate) parking space
     * 
     * We don't actually delete from database (soft delete)
     * We just set isActive to false
     * 
     * @param id Parking space ID
     * @return Success message
     * @throws RuntimeException if not found or user not authorized
     */
    @Transactional
    public String deleteParkingSpace(Long id) {
        User currentUser = authService.getCurrentUser();

        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));

        // Check ownership
        if (!parkingSpace.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this parking space");
        }

        // Soft delete
        parkingSpace.setIsActive(false);
        parkingSpaceRepository.save(parkingSpace);

        return "Parking space deleted successfully";
    }

    /**
     * Search parking spaces by city
     * 
     * Public endpoint - anyone can search
     * 
     * @param city City name (partial match allowed)
     * @return List of parking spaces
     */
    public List<ParkingSpace> searchByCity(String city) {
        return parkingSpaceRepository.searchByCity(city);
    }

    /**
     * Search parking spaces by city and area
     * 
     * @param city City name
     * @param area Area name
     * @return List of parking spaces
     */
    public List<ParkingSpace> searchByCityAndArea(String city, String area) {
        return parkingSpaceRepository.searchByCityAndArea(city, area);
    }

    /**
     * Get all active parking spaces
     * 
     * @return List of active parking spaces
     */
    public List<ParkingSpace> getAllActiveParkingSpaces() {
        return parkingSpaceRepository.findByIsActiveTrue();
    }

    /**
     * Get parking space by ID
     * 
     * @param id Parking space ID
     * @return Parking space
     * @throws RuntimeException if not found
     */
    public ParkingSpace getParkingSpaceById(Long id) {
        return parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking space not found"));
    }

    /**
     * Get owner's parking spaces
     * 
     * Shows all parking spaces created by current owner
     * 
     * @return List of owner's parking spaces
     */
    public List<ParkingSpace> getMyParkingSpaces() {
        User currentUser = authService.getCurrentUser();
        return parkingSpaceRepository.findByOwnerId(currentUser.getId());
    }
}
