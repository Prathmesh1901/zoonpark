package com.parkingbooking.dto.response;

/**
 * MessageResponse DTO - Generic response for success/error messages
 * 
 * Used for simple operations like:
 * - Booking cancellation
 * - Account approval
 * - Delete operations
 */
public class MessageResponse {

    private String message;

    // Constructors
    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
