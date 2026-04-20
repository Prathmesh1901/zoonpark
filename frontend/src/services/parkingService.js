import api from './api';

/**
 * Parking Space Service
 * 
 * Contains all parking space-related API calls
 */

const parkingService = {
    /**
     * Search parking spaces
     * 
     * @param {Object} filters - Search filters (city, area)
     * @returns {Promise} List of parking spaces
     */
    searchParkingSpaces: async (filters = {}) => {
        const params = new URLSearchParams();
        if (filters.city) params.append('city', filters.city);
        if (filters.area) params.append('area', filters.area);

        const response = await api.get(`/parking-spaces?${params.toString()}`);
        return response.data;
    },

    /**
     * Get parking space by ID
     * 
     * @param {number} id - Parking space ID
     * @returns {Promise} Parking space details
     */
    getParkingSpaceById: async (id) => {
        const response = await api.get(`/parking-spaces/${id}`);
        return response.data;
    },

    /**
     * Create parking space (OWNER only)
     * 
     * @param {Object} parkingData - Parking space data
     * @returns {Promise} Created parking space
     */
    createParkingSpace: async (parkingData) => {
        const response = await api.post('/parking-spaces', parkingData);
        return response.data;
    },

    /**
     * Update parking space (OWNER only)
     * 
     * @param {number} id - Parking space ID
     * @param {Object} parkingData - Updated data
     * @returns {Promise} Updated parking space
     */
    updateParkingSpace: async (id, parkingData) => {
        const response = await api.put(`/parking-spaces/${id}`, parkingData);
        return response.data;
    },

    /**
     * Delete parking space (OWNER only)
     * 
     * @param {number} id - Parking space ID
     * @returns {Promise} Success message
     */
    deleteParkingSpace: async (id) => {
        const response = await api.delete(`/parking-spaces/${id}`);
        return response.data;
    },

    /**
     * Get owner's parking spaces
     * 
     * @returns {Promise} List of owner's parking spaces
     */
    getMyParkingSpaces: async () => {
        const response = await api.get('/parking-spaces/my-spaces');
        return response.data;
    }
};

export default parkingService;
