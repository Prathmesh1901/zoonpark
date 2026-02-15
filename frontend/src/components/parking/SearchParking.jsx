import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { FiSearch, FiMapPin, FiClock, FiDollarSign, FiLayout } from 'react-icons/fi';
import parkingService from '../../services/parkingService';

/**
 * Modern SearchParking Component
 * 
 * Features:
 * - Animated search bar
 * - Grid layout for results
 * - Data visualization with icons
 */
const SearchParking = () => {
    const [filters, setFilters] = useState({
        city: '',
        area: ''
    });
    const [parkingSpaces, setParkingSpaces] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        searchParkingSpaces();
    }, []);

    const handleChange = (e) => {
        setFilters({
            ...filters,
            [e.target.name]: e.target.value
        });
    };

    const searchParkingSpaces = async () => {
        setLoading(true);
        setError('');
        try {
            const data = await parkingService.searchParkingSpaces(filters);
            setParkingSpaces(data);
        } catch (err) {
            console.error('Search error:', err);
            const errorMessage = err.response?.data?.message || err.message || 'Failed to load parking spaces';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        searchParkingSpaces();
    };

    return (
        <div className="search-container" style={{ maxWidth: '1200px', margin: '0 auto' }}>
            <motion.div
                className="search-header text-center"
                initial={{ opacity: 0, y: -20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
            >
                <h1 style={{ marginBottom: '2rem', fontSize: '2.5rem' }}>Find Your Perfect Spot</h1>

                <form onSubmit={handleSubmit} className="search-form glass-panel" style={{
                    display: 'flex',
                    gap: '1rem',
                    background: 'var(--bg-secondary)',
                    padding: '1.5rem',
                    borderRadius: 'var(--radius-lg)',
                    boxShadow: 'var(--glass-shadow)',
                    flexWrap: 'wrap'
                }}>
                    <div className="input-group" style={{ flex: 1, minWidth: '250px', position: 'relative' }}>
                        <FiMapPin style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                        <input
                            type="text"
                            name="city"
                            placeholder="City (e.g., Mumbai)"
                            value={filters.city}
                            onChange={handleChange}
                            style={{ paddingLeft: '3rem' }}
                        />
                    </div>
                    <div className="input-group" style={{ flex: 1, minWidth: '250px', position: 'relative' }}>
                        <FiMapPin style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                        <input
                            type="text"
                            name="area"
                            placeholder="Area (e.g., Andheri)"
                            value={filters.area}
                            onChange={handleChange}
                            style={{ paddingLeft: '3rem' }}
                        />
                    </div>
                    <motion.button
                        type="submit"
                        className="btn btn-primary"
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        disabled={loading}
                    >
                        {loading ? 'Searching...' : <><FiSearch /> Search</>}
                    </motion.button>
                </form>
            </motion.div>

            {error && <motion.div className="error-message" initial={{ opacity: 0 }} animate={{ opacity: 1 }}>{error}</motion.div>}

            <div className="parking-results" style={{ marginTop: '3rem' }}>
                {loading ? (
                    <div className="text-center" style={{ padding: '2rem', color: 'var(--text-secondary)' }}>
                        Searching nearby spaces...
                    </div>
                ) : (
                    <motion.div
                        className="grid-cards"
                        initial="hidden"
                        animate="visible"
                        variants={{
                            hidden: { opacity: 0 },
                            visible: { opacity: 1, transition: { staggerChildren: 0.1 } }
                        }}
                    >
                        <AnimatePresence>
                            {parkingSpaces.length === 0 ? (
                                <motion.div
                                    className="no-results text-center"
                                    style={{ gridColumn: '1 / -1', padding: '3rem', color: 'var(--text-secondary)' }}
                                    initial={{ opacity: 0 }}
                                    animate={{ opacity: 1 }}
                                >
                                    <FiSearch size={48} style={{ marginBottom: '1rem', opacity: 0.5 }} />
                                    <p>No parking spaces found in this area.</p>
                                </motion.div>
                            ) : (
                                parkingSpaces.map((parking) => (
                                    <ParkingCard key={parking.id} parking={parking} />
                                ))
                            )}
                        </AnimatePresence>
                    </motion.div>
                )}
            </div>
        </div>
    );
};

const ParkingCard = ({ parking }) => (
    <motion.div
        className="card parking-card"
        variants={{
            hidden: { opacity: 0, y: 20 },
            visible: { opacity: 1, y: 0 }
        }}
        whileHover={{ y: -5 }}
        style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}
    >
        <div className="card-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 'bold' }}>{parking.name}</h3>
            <span className="price-tag" style={{
                background: 'rgba(34, 197, 94, 0.1)',
                color: 'var(--success)',
                padding: '0.25rem 0.75rem',
                borderRadius: 'var(--radius-full)',
                fontWeight: 'bold'
            }}>
                ₹{parking.pricePerHour}/hr
            </span>
        </div>

        <div className="card-body" style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <FiMapPin /> {parking.area}, {parking.city}
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <FiLayout /> {parking.totalSlots} Slots Total
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <FiClock /> {parking.openingTime} - {parking.closingTime}
            </div>
        </div>

        <Link to={`/booking/${parking.id}`} className="btn btn-primary" style={{ marginTop: 'auto', justifyContent: 'center' }}>
            Book Now
        </Link>
    </motion.div>
);

export default SearchParking;
