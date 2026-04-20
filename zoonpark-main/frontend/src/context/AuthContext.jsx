import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';

/**
 * AuthContext - Global authentication state
 * 
 * This context provides authentication state and functions to all components
 * 
 * Why use Context?
 * - Avoid prop drilling (passing props through many levels)
 * - Share authentication state across entire app
 * - Centralized authentication logic
 * 
 * Usage in components:
 * const { user, login, logout } = useAuth();
 */

const AuthContext = createContext(null);

/**
 * AuthProvider Component
 * 
 * Wraps the entire app and provides authentication state
 * 
 * @param {Object} props - Component props
 * @param {ReactNode} props.children - Child components
 */
export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    /**
     * Load user from localStorage on mount
     */
    useEffect(() => {
        const storedUser = authService.getStoredUser();
        if (storedUser) {
            setUser(storedUser);
        }
        setLoading(false);
    }, []);

    /**
     * Login function
     * 
     * @param {Object} credentials - Email and password
     * @returns {Promise} Login response
     */
    const login = async (credentials) => {
        const userData = await authService.login(credentials);
        setUser(userData);
        return userData;
    };

    /**
     * Register function
     * 
     * @param {Object} userData - Registration data
     * @returns {Promise} Registration response
     */
    const register = async (userData) => {
        const newUser = await authService.register(userData);
        setUser(newUser);
        return newUser;
    };

    /**
     * Logout function
     */
    const logout = () => {
        authService.logout();
        setUser(null);
    };

    /**
     * Check if user is authenticated
     * 
     * @returns {boolean} True if user is logged in
     */
    const isAuthenticated = () => {
        return !!user;
    };

    /**
     * Check if user has specific role
     * 
     * @param {string} role - Role name (CUSTOMER, OWNER, ADMIN)
     * @returns {boolean} True if user has the role
     */
    const hasRole = (role) => {
        return user && user.role === role;
    };

    const value = {
        user,
        loading,
        login,
        register,
        logout,
        isAuthenticated,
        hasRole
    };

    return (
        <AuthContext.Provider value={value}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

/**
 * useAuth Hook
 * 
 * Custom hook to access authentication context
 * 
 * @returns {Object} Authentication context value
 * @throws {Error} If used outside AuthProvider
 */
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};

export default AuthContext;
