import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/common/ProtectedRoute';
import Navbar from './components/common/Navbar';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import Home from './components/Home';
import SearchParking from './components/parking/SearchParking';
import BookingPage from './components/booking/BookingPage';
import UserDashboard from './components/dashboard/UserDashboard';
import OwnerDashboard from './components/dashboard/OwnerDashboard';
import AdminDashboard from './components/dashboard/AdminDashboard';

/**
 * App Component - Main application component
 * 
 * This component:
 * 1. Wraps the app with AuthProvider (provides authentication state)
 * 2. Sets up routing with React Router
 * 3. Defines all application routes
 * 4. Protects routes that require authentication
 * 
 * Route Structure:
 * - Public routes: /, /login, /register, /search
 * - Protected routes: /dashboard, /booking/:id
 * - Role-specific routes: /owner/dashboard, /admin/dashboard
 */
function App() {
    return (
        <AuthProvider>
            <Router future={{ v7_startTransition: true }}>
                <div className="app">
                    <Navbar />
                    <div className="main-content">
                        <Routes>
                            {/* Public Routes */}
                            <Route path="/" element={<Home />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/search" element={<SearchParking />} />

                            {/* Protected Routes - Require Authentication */}
                            <Route
                                path="/dashboard"
                                element={
                                    <ProtectedRoute>
                                        <UserDashboard />
                                    </ProtectedRoute>
                                }
                            />
                            <Route
                                path="/booking/:id"
                                element={
                                    <ProtectedRoute>
                                        <BookingPage />
                                    </ProtectedRoute>
                                }
                            />

                            {/* Owner Routes - Require OWNER role */}
                            <Route
                                path="/owner/dashboard"
                                element={
                                    <ProtectedRoute requiredRole="OWNER">
                                        <OwnerDashboard />
                                    </ProtectedRoute>
                                }
                            />

                            {/* Admin Routes - Require ADMIN role */}
                            <Route
                                path="/admin/dashboard"
                                element={
                                    <ProtectedRoute requiredRole="ADMIN">
                                        <AdminDashboard />
                                    </ProtectedRoute>
                                }
                            />

                            {/* Catch all - redirect to home */}
                            <Route path="*" element={<Navigate to="/" replace />} />
                        </Routes>
                    </div>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
