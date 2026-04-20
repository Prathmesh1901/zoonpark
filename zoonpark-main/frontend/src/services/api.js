import axios from 'axios';

/**
 * Axios instance for API calls
 * 
 * This is a configured axios instance that:
 * 1. Sets the base URL for all API calls
 * 2. Automatically includes JWT token in headers
 * 3. Handles authentication errors
 * 
 * Usage:
 * import api from './services/api';
 * const response = await api.get('/parking-spaces');
 */

// Create axios instance with base configuration
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    headers: {
        'Content-Type': 'application/json'
    },
    timeout: 10000 // 10 second timeout
});

/**
 * Request interceptor
 * 
 * Runs BEFORE every request
 * Adds JWT token to Authorization header if it exists
 */
api.interceptors.request.use(
    (config) => {
        // Get token from localStorage
        const token = localStorage.getItem('token');

        // If token exists, add it to headers
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

/**
 * Response interceptor
 * 
 * Runs AFTER every response
 * Handles authentication errors (401) and provides better error messages
 */
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        // Log error details for debugging
        if (error.response) {
            // Server responded with error status
            console.error('API Error:', {
                status: error.response.status,
                data: error.response.data,
                url: error.config?.url
            });
        } else if (error.request) {
            // Request made but no response received
            console.error('Network Error: No response from server', {
                url: error.config?.url,
                message: 'Backend server may not be running on http://localhost:8080'
            });
        } else {
            // Error in request setup
            console.error('Request Error:', error.message);
        }

        // If 401 Unauthorized, clear token and redirect to login
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }

        return Promise.reject(error);
    }
);

export default api;
