import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { FiAlertCircle, FiArrowRight } from 'react-icons/fi';
import { useAuth } from '../../context/AuthContext';

const ChooseRole = () => {
    const [loadingRole, setLoadingRole] = useState('');
    const [error, setError] = useState('');
    const { updateRole } = useAuth();
    const navigate = useNavigate();

    const handleChoice = async (role) => {
        setError('');
        setLoadingRole(role);

        try {
            await updateRole(role);
            navigate(role === 'OWNER' ? '/owner/dashboard' : '/search');
        } catch (err) {
            console.error('Role update error:', err);
            setError(err.response?.data?.message || err.response?.data || 'Could not save your choice. Please try again.');
        } finally {
            setLoadingRole('');
        }
    };

    return (
        <div className="auth-container">
            <motion.div
                className="choice-panel"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.4 }}
            >
                <div className="text-center mb-4">
                    <h2>How do you want to use ZoomPark?</h2>
                    <p className="choice-subtitle">Choose one to continue. You can switch later by coming back to this page.</p>
                </div>

                {error && (
                    <div className="error-message">
                        <FiAlertCircle /> {error}
                    </div>
                )}

                <div className="choice-grid">
                    <button
                        type="button"
                        className="choice-card"
                        onClick={() => handleChoice('CUSTOMER')}
                        disabled={!!loadingRole}
                    >
                        <span className="choice-kicker">Find a spot</span>
                        <strong>Book parking space</strong>
                        <p>Search available spaces, reserve a slot, and manage your bookings.</p>
                        <span className="choice-action">
                            {loadingRole === 'CUSTOMER' ? 'Saving...' : <>Continue <FiArrowRight /></>}
                        </span>
                    </button>

                    <button
                        type="button"
                        className="choice-card"
                        onClick={() => handleChoice('OWNER')}
                        disabled={!!loadingRole}
                    >
                        <span className="choice-kicker">Earn from space</span>
                        <strong>List my parking space</strong>
                        <p>Add your driveway, garage, or lot and manage customer bookings.</p>
                        <span className="choice-action">
                            {loadingRole === 'OWNER' ? 'Saving...' : <>Continue <FiArrowRight /></>}
                        </span>
                    </button>
                </div>
            </motion.div>
        </div>
    );
};

export default ChooseRole;
