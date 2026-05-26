import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import {
    FiHome, FiSearch, FiCalendar, FiUser,
    FiLogOut, FiMap, FiMenu, FiX, FiGrid, FiMoon, FiSun
} from 'react-icons/fi';
import { FaCarSide } from 'react-icons/fa';
import logo from '../../assets/logg.png';

/**
 * Modern Navbar Component
 * 
 * Features:
 * - Glassmorphism design
 * - Animated links
 * - Mobile responsive menu
 * - Vector icons
 */
const Navbar = () => {
    const { user, logout, isAuthenticated } = useAuth();
    const [isOpen, setIsOpen] = useState(false);
    const [theme, setTheme] = useState(() => localStorage.getItem('theme') || 'light');
    const location = useLocation();

    React.useEffect(() => {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    }, [theme]);

    const handleLogout = () => {
        logout();
        window.location.href = '/';
    };

    const toggleMenu = () => setIsOpen(!isOpen);
    const toggleTheme = () => setTheme((currentTheme) => currentTheme === 'dark' ? 'light' : 'dark');

    const isActive = (path) => location.pathname === path ? 'active' : '';

    const navVariants = {
        hidden: { opacity: 0, y: -20 },
        visible: { opacity: 1, y: 0, transition: { duration: 0.5 } }
    };

    const linkVatiants = {
        hover: { scale: 1.05, color: '#3b82f6' },
        tap: { scale: 0.95 }
    };

    return (
        <motion.nav
            className="navbar"
            initial="hidden"
            animate="visible"
            variants={navVariants}
        >
            <div className="nav-container">

                <Link to="/" className="nav-logo">
                    <img
                        src={logo}
                        alt="ZoomPark"
                        className="nav-logo-img"
                    />
                </Link>

                {/* Mobile Menu Button */}
                <button className="mobile-menu-btn" onClick={toggleMenu}>
                    {isOpen ? <FiX size={24} /> : <FiMenu size={24} />}
                </button>

                {/* Desktop Menu */}
                <ul className={`nav-menu ${isOpen ? 'active' : ''}`}>
                    <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                        <Link to="/" className={`nav-link ${isActive('/')}`}>
                            <FiHome /> Home
                        </Link>
                    </motion.li>

                    <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                        <Link to="/search" className={`nav-link ${isActive('/search')}`}>
                            <FiSearch /> Search
                        </Link>
                    </motion.li>

                    {isAuthenticated() ? (
                        <>
                            {user.role === 'CUSTOMER' && (
                                <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                                    <Link to="/dashboard" className={`nav-link ${isActive('/dashboard')}`}>
                                        <FiCalendar /> My Bookings
                                    </Link>
                                </motion.li>
                            )}

                            {user.role === 'OWNER' && (
                                <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                                    <Link to="/owner/dashboard" className={`nav-link ${isActive('/owner/dashboard')}`}>
                                        <FiMap /> My Spaces
                                    </Link>
                                </motion.li>
                            )}

                            {user.role === 'ADMIN' && (
                                <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                                    <Link to="/admin/dashboard" className={`nav-link ${isActive('/admin/dashboard')}`}>
                                        <FiGrid /> Admin Panel
                                    </Link>
                                </motion.li>
                            )}

                            <li className="user-profile">
                                <div className="user-info">
                                    <FiUser />
                                    <span>{user.fullName}</span>
                                </div>
                            </li>

                            <li>
                                <motion.button
                                    type="button"
                                    onClick={toggleTheme}
                                    className="theme-toggle-btn"
                                    aria-label={theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode'}
                                    title={theme === 'dark' ? 'Light mode' : 'Dark mode'}
                                    whileHover={{ scale: 1.05 }}
                                    whileTap={{ scale: 0.95 }}
                                >
                                    {theme === 'dark' ? <FiSun /> : <FiMoon />}
                                </motion.button>
                            </li>

                            <li>
                                <motion.button
                                    onClick={handleLogout}
                                    className="btn btn-danger btn-sm"
                                    whileHover={{ scale: 1.05 }}
                                    whileTap={{ scale: 0.95 }}
                                >
                                    <FiLogOut /> Logout
                                </motion.button>
                            </li>
                        </>
                    ) : (
                        <>
                            <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                                <Link to="/login" className={`nav-link ${isActive('/login')}`}>
                                    <FiUser /> Login
                                </Link>
                            </motion.li>

                            <motion.li variants={linkVatiants} whileHover="hover" whileTap="tap">
                                <Link to="/register" className="btn btn-primary">
                                    Register
                                </Link>
                            </motion.li>
                        </>
                    )}
                </ul>
            </div>
        </motion.nav>
    );
};

export default Navbar;
