import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FiPlus, FiTrash2, FiMapPin, FiClock, FiLayout, FiDollarSign, FiX } from 'react-icons/fi';
import parkingService from '../../services/parkingService';

/**
 * Modern OwnerDashboard Component
 */
const OwnerDashboard = () => {
    const [parkingSpaces, setParkingSpaces] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState({
        name: '',
        city: '',
        area: '',
        address: '',
        totalSlots: '',
        pricePerHour: '',
        openingTime: '',
        closingTime: ''
    });

    useEffect(() => {
        loadParkingSpaces();
    }, []);

    const loadParkingSpaces = async () => {
        try {
            const data = await parkingService.getMyParkingSpaces();
            setParkingSpaces(data);
        } catch (err) {
            alert('Failed to load parking spaces');
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await parkingService.createParkingSpace(formData);
            alert('Parking space created successfully!');
            setShowForm(false);
            loadParkingSpaces();
            setFormData({
                name: '',
                city: '',
                area: '',
                address: '',
                totalSlots: '',
                pricePerHour: '',
                openingTime: '',
                closingTime: ''
            });
        } catch (err) {
            const errorMessage = err.response?.data || err.response?.data?.message || err.message || 'Failed to create parking space';
            alert(errorMessage);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm('Are you sure you want to delete this parking space?')) {
            return;
        }

        try {
            await parkingService.deleteParkingSpace(id);
            loadParkingSpaces();
        } catch (err) {
            alert('Failed to delete parking space');
        }
    };

    return (
        <div className="dashboard-container" style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
                <h1>My Parking Spaces</h1>
                <motion.button
                    onClick={() => setShowForm(!showForm)}
                    className={`btn ${showForm ? 'btn-danger' : 'btn-primary'}`}
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                >
                    {showForm ? <><FiX /> Cancel</> : <><FiPlus /> Add New Space</>}
                </motion.button>
            </div>

            <AnimatePresence>
                {showForm && (
                    <motion.div
                        initial={{ opacity: 0, height: 0 }}
                        animate={{ opacity: 1, height: 'auto' }}
                        exit={{ opacity: 0, height: 0 }}
                        style={{ overflow: 'hidden', marginBottom: '2rem' }}
                    >
                        <form
                            onSubmit={handleSubmit}
                            className="card"
                            style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1.5rem' }}
                        >
                            <div className="form-group">
                                <label>Parking Name</label>
                                <input type="text" name="name" value={formData.name} onChange={handleChange} required placeholder="e.g. Downtown Plaza" />
                            </div>
                            <div className="form-group">
                                <label>City</label>
                                <input type="text" name="city" value={formData.city} onChange={handleChange} required placeholder="City" />
                            </div>
                            <div className="form-group">
                                <label>Area</label>
                                <input type="text" name="area" value={formData.area} onChange={handleChange} required placeholder="Area" />
                            </div>
                            <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                                <label>Full Address</label>
                                <textarea name="address" value={formData.address} onChange={handleChange} required placeholder="Enter full address" rows="2" />
                            </div>

                            <div className="form-group">
                                <label>Total Slots</label>
                                <div className="input-with-icon">
                                    <input type="number" name="totalSlots" value={formData.totalSlots} onChange={handleChange} required min="1" placeholder="e.g. 50" />
                                </div>
                            </div>
                            <div className="form-group">
                                <label>Price per Hour (₹)</label>
                                <input type="number" name="pricePerHour" value={formData.pricePerHour} onChange={handleChange} required min="0" step="0.01" placeholder="e.g. 20.00" />
                            </div>
                            <div className="form-group">
                                <label>Opening Time</label>
                                <input type="time" name="openingTime" value={formData.openingTime} onChange={handleChange} required />
                            </div>
                            <div className="form-group">
                                <label>Closing Time</label>
                                <input type="time" name="closingTime" value={formData.closingTime} onChange={handleChange} required />
                            </div>

                            <motion.button
                                type="submit"
                                className="btn btn-primary"
                                style={{ gridColumn: '1 / -1', justifyContent: 'center' }}
                                whileHover={{ scale: 1.02 }}
                                whileTap={{ scale: 0.98 }}
                            >
                                <FiPlus /> Create Parking Space
                            </motion.button>
                        </form>
                    </motion.div>
                )}
            </AnimatePresence>

            <div className="grid-cards">
                {parkingSpaces.length === 0 && !showForm ? (
                    <div style={{ gridColumn: '1 / -1', textAlign: 'center', padding: '4rem', color: 'var(--text-secondary)' }}>
                        <p>No parking spaces found. Add one to get started!</p>
                    </div>
                ) : (
                    parkingSpaces.map((parking) => (
                        <motion.div
                            key={parking.id}
                            className="card parking-card"
                            initial={{ opacity: 0, scale: 0.9 }}
                            animate={{ opacity: 1, scale: 1 }}
                            whileHover={{ y: -5 }}
                        >
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: '1rem' }}>
                                <h3 style={{ fontSize: '1.25rem' }}>{parking.name}</h3>
                                <span className={`status ${parking.isActive ? 'status-confirmed' : 'status-cancelled'}`}>
                                    {parking.isActive ? 'Active' : 'Inactive'}
                                </span>
                            </div>

                            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', marginBottom: '1.5rem' }}>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)' }}>
                                    <FiMapPin className="text-primary" /> {parking.area}, {parking.city}
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)' }}>
                                    <FiLayout className="text-secondary" /> {parking.totalSlots} Slots
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)' }}>
                                    <FiDollarSign className="text-success" /> ₹{parking.pricePerHour}/hr
                                </div>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)' }}>
                                    <FiClock className="text-warning" /> {parking.openingTime} - {parking.closingTime}
                                </div>
                            </div>

                            <motion.button
                                onClick={() => handleDelete(parking.id)}
                                className="btn btn-danger"
                                style={{ width: '100%', justifyContent: 'center' }}
                                whileHover={{ scale: 1.02 }}
                                whileTap={{ scale: 0.98 }}
                            >
                                <FiTrash2 /> Delete Space
                            </motion.button>
                        </motion.div>
                    ))
                )}
            </div>
        </div>
    );
};

export default OwnerDashboard;
