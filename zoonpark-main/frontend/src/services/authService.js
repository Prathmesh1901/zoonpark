import api from './api';

/**
 * Authentication Service
 * 
 * Contains all authentication-related API calls:
 * - register()
 * - login()
 * - getCurrentUser()
 * - logout()
 */

const authService = {
    /**
     * Register a new user
     * 
     * @param {Object} userData - User registration data
     * @returns {Promise} API response with token and user info
     */
    register: async (userData) => {
        const response = await api.post('/auth/register', userData);

        // Save token and user to localStorage
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
    },

    /**
     * Login user
     * 
     * @param {Object} credentials - Email and password
     * @returns {Promise} API response with token and user info
     */
    login: async (credentials) => {
        const response = await api.post('/auth/login', credentials);

        // Save token and user to localStorage
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify(response.data));
        }

        return response.data;
    },

    /**
     * Get current user information
     * 
     * @returns {Promise} Current user data
     */
    getCurrentUser: async () => {
        const response = await api.get('/auth/me');
        return response.data;
    },

    /**
     * Logout user
     * 
     * Clears token and user from localStorage
     */
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },

    /**
     * Get stored user from localStorage
     * 
     * @returns {Object|null} User object or null
     */
    getStoredUser: () => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    /**
     * Check if user is authenticated
     * 
     * @returns {boolean} True if token exists
     */
    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    }
};

export default authService;
