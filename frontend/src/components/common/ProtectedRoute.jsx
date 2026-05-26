import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

/**
 * ProtectedRoute Component
 * 
 * Protects routes that require authentication
 * 
 * How it works:
 * 1. Check if user is authenticated
 * 2. If yes, render the children (protected component)
 * 3. If no, redirect to login page
 * 
 * Usage:
 * <Route path="/dashboard" element={
 *   <ProtectedRoute>
 *     <Dashboard />
 *   </ProtectedRoute>
 * } />
 * 
 * @param {Object} props - Component props
 * @param {ReactNode} props.children - Protected component
 * @param {string} props.requiredRole - Optional required role
 */
const ProtectedRoute = ({ children, requiredRole }) => {
    const { isAuthenticated, hasRole, user } = useAuth();

    // Check if user is authenticated
    if (!isAuthenticated()) {
        return <Navigate to="/login" replace />;
    }

    // Check if specific role is required
    if (requiredRole && !hasRole(requiredRole)) {
        return <Navigate to="/" replace />;
    }

    // If authenticated (and has required role if specified), render children
    return children;
};

export default ProtectedRoute;
