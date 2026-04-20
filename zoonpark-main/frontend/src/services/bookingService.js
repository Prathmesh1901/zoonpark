import api from './api';

/**
 * Booking Service
 * 
 * Contains all booking-related API calls
 */

const bookingService = {
    /**
     * Check availability
     * 
     * @param {Object} params - parkingSpaceId, date, startTime, endTime
     * @returns {Promise} Availability status and available slots
     */
    checkAvailability: async (params) => {
        const response = await api.get('/bookings/check-availability', { params });
        return response.data;
    },

    /**
     * Calculate booking price
     * 
     * @param {Object} params - parkingSpaceId, startTime, endTime
     * @returns {Promise} Price calculation
     */
    calculatePrice: async (params) => {
        const response = await api.get('/bookings/calculate-price', { params });
        return response.data;
    },

    /**
     * Create booking
     * 
     * @param {Object} bookingData - Booking details
     * @returns {Promise} Created booking
     */
    createBooking: async (bookingData) => {
        const response = await api.post('/bookings', bookingData);
        return response.data;
    },

    /**
     * Get booking by ID
     * 
     * @param {string} bookingId - Booking ID
     * @returns {Promise} Booking details
     */
    getBookingById: async (bookingId) => {
        const response = await api.get(`/bookings/${bookingId}`);
        return response.data;
    },

    /**
     * Cancel booking
     * 
     * @param {string} bookingId - Booking ID
     * @returns {Promise} Success message
     */
    cancelBooking: async (bookingId) => {
        const response = await api.put(`/bookings/${bookingId}/cancel`);
        return response.data;
    },

    /**
     * Get user's bookings
     * 
     * @returns {Promise} List of user's bookings
     */
    getMyBookings: async () => {
        const response = await api.get('/bookings/my-bookings');
        return response.data;
    }
};

export default bookingService;
