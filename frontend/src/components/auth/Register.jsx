import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { FiUser, FiMail, FiLock, FiPhone, FiUserCheck, FiAlertCircle, FiArrowRight } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';

/**
 * Modern Register Component
 */
const Register = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        fullName: '',
        phone: '',
        role: 'CUSTOMER'
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { register } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await register(formData);

            // Redirect based on role
            if (formData.role === 'OWNER') {
                alert('Registration successful! Your account is pending admin approval.');
                navigate('/owner/dashboard');
            } else {
                navigate('/dashboard');
            }
        } catch (err) {
            console.error('Registration error:', err);
            const errorMessage = err.response?.data?.message || err.response?.data || 'Registration failed. Please try again.';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <motion.div
                className="auth-card register"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
            >
                <div className="text-center mb-4">
                    <div style={{
                        background: 'rgba(139, 92, 246, 0.1)',
                        width: '60px',
                        height: '60px',
                        borderRadius: '50%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        margin: '0 auto 1rem',
                        color: 'var(--accent)'
                    }}>
                        <FiUserCheck size={24} />
                    </div>
                    <h2 style={{ fontSize: '1.8rem', fontWeight: 'bold' }}>Create Account</h2>
                    <p style={{ color: 'var(--text-secondary)' }}>Join us to book or list parking spaces</p>
                </div>

                {error && (
                    <motion.div
                        className="error-message"
                        initial={{ opacity: 0, height: 0 }}
                        animate={{ opacity: 1, height: 'auto' }}
                    >
                        <FiAlertCircle /> {error}
                    </motion.div>
                )}

                <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>

                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label>Full Name</label>
                        <div className="input-group" style={{ position: 'relative' }}>
                            <FiUser style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                            <input
                                type="text"
                                name="fullName"
                                value={formData.fullName}
                                onChange={handleChange}
                                required
                                placeholder="Enter your full name"
                                style={{ paddingLeft: '3rem' }}
                            />
                        </div>
                    </div>

                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label>Email</label>
                        <div className="input-group" style={{ position: 'relative' }}>
                            <FiMail style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                                placeholder="Enter your email"
                                style={{ paddingLeft: '3rem' }}
                            />
                        </div>
                    </div>

                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label>Password</label>
                        <div className="input-group" style={{ position: 'relative' }}>
                            <FiLock style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                                minLength="6"
                                placeholder="Min 6 characters"
                                style={{ paddingLeft: '3rem' }}
                            />
                        </div>
                    </div>

                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label>Phone</label>
                        <div className="input-group" style={{ position: 'relative' }}>
                            <FiPhone style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                            <input
                                type="tel"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                                placeholder="Enter your phone number"
                                style={{ paddingLeft: '3rem' }}
                            />
                        </div>
                    </div>

                    <div className="form-group" style={{ marginBottom: 0 }}>
                        <label>I want to...</label>
                        <div className="input-group" style={{ position: 'relative' }}>
                            <select
                                name="role"
                                value={formData.role}
                                onChange={handleChange}
                                style={{
                                    appearance: 'none',
                                    padding: '0.75rem 1rem',
                                    width: '100%',
                                    background: 'var(--bg-primary)',
                                    color: 'var(--text-primary)',
                                    borderRadius: 'var(--radius-md)',
                                    border: '1px solid var(--bg-tertiary)'
                                }}
                            >
                                <option value="CUSTOMER">🚗 Book Parking Spaces</option>
                                <option value="OWNER">🅿️ List My Parking Space</option>
                            </select>
                        </div>
                    </div>

                    <motion.button
                        type="submit"
                        className="btn btn-primary"
                        style={{ width: '100%', justifyContent: 'center', marginTop: '0.5rem' }}
                        disabled={loading}
                        whileHover={{ scale: 1.02 }}
                        whileTap={{ scale: 0.98 }}
                    >
                        {loading ? 'Creating Account...' : <><FiArrowRight /> Register</>}
                    </motion.button>
                </form>

                <p className="auth-link" style={{ marginTop: '1.5rem', color: 'var(--text-secondary)' }}>
                    Already have an account? <Link to="/login" style={{ color: 'var(--primary)', fontWeight: '600' }}>Login here</Link>
                </p>
            </motion.div>
        </div>
    );
};

export default Register;
