import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { FiCalendar, FiClock, FiMapPin, FiDollarSign, FiCheckCircle, FiAlertCircle } from 'react-icons/fi';
import parkingService from '../../services/parkingService';
import bookingService from '../../services/bookingService';

/**
 * Modern BookingPage Component
 * 
 * Features:
 * - Glassmorphism card design
 * - Animated form interactions
 * - Clear status indicators
 * - Responsive layout
 */
const BookingPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [parking, setParking] = useState(null);
    const [bookingData, setBookingData] = useState({
        parkingSpaceId: id,
        bookingDate: '',
        startTime: '',
        endTime: ''
    });
    const [availability, setAvailability] = useState(null);
    const [price, setPrice] = useState(null);
    const [loading, setLoading] = useState(false);
    const [pageLoading, setPageLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        loadParkingSpace();
    }, [id]);

    const loadParkingSpace = async () => {
        try {
            const data = await parkingService.getParkingSpaceById(id);
            setParking(data);
            setBookingData({ ...bookingData, parkingSpaceId: data.id });
        } catch (err) {
            setError('Failed to load parking space');
        } finally {
            setPageLoading(false);
        }
    };

    const handleChange = (e) => {
        setBookingData({
            ...bookingData,
            [e.target.name]: e.target.value
        });
    };

    const checkAvailability = async () => {
        if (!bookingData.bookingDate || !bookingData.startTime || !bookingData.endTime) {
            return;
        }

        try {
            const availData = await bookingService.checkAvailability({
                parkingSpaceId: id,
                date: bookingData.bookingDate,
                startTime: bookingData.startTime,
                endTime: bookingData.endTime
            });
            setAvailability(availData);

            if (availData.available) {
                const priceData = await bookingService.calculatePrice({
                    parkingSpaceId: id,
                    startTime: bookingData.startTime,
                    endTime: bookingData.endTime
                });
                setPrice(priceData.totalPrice);
            } else {
                setPrice(null);
            }
        } catch (err) {
            console.error(err);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            await bookingService.createBooking(bookingData);
            alert('Booking created successfully!');
            navigate('/dashboard');
        } catch (err) {
            console.error('Booking error:', err);
            const errorMessage = err.response?.data?.message || err.response?.data || 'Failed to create booking';
            setError(errorMessage);
        } finally {
            setLoading(false);
        }
    };

    if (pageLoading) return <div className="text-center" style={{ padding: '4rem' }}>Loading details...</div>;
    if (!parking) return <div className="error-message">Parking space not found</div>;

    return (
        <motion.div
            className="booking-container"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            style={{ maxWidth: '800px', margin: '0 auto' }}
        >
            <h1 className="text-center" style={{ marginBottom: '2rem' }}>Confirm Your Booking</h1>

            <div className="booking-layout" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
                {/* Parking Details Card */}
                <motion.div
                    className="card parking-details"
                    initial={{ x: -20, opacity: 0 }}
                    animate={{ x: 0, opacity: 1 }}
                    transition={{ delay: 0.2 }}
                >
                    <h2 style={{ marginBottom: '1.5rem', borderBottom: '1px solid var(--glass-border)', paddingBottom: '1rem' }}>
                        {parking.name}
                    </h2>
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                        <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center', color: 'var(--text-secondary)' }}>
                            <FiMapPin size={20} color="var(--primary)" />
                            <span>{parking.address}, {parking.area}, {parking.city}</span>
                        </div>
                        <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center', color: 'var(--text-secondary)' }}>
                            <FiDollarSign size={20} color="var(--success)" />
                            <span style={{ fontSize: '1.2rem', fontWeight: 'bold', color: 'var(--success)' }}>₹{parking.pricePerHour}/hr</span>
                        </div>
                        <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center', color: 'var(--text-secondary)' }}>
                            <FiClock size={20} color="var(--warning)" />
                            <span>Operating Hours: {parking.openingTime} - {parking.closingTime}</span>
                        </div>
                    </div>
                </motion.div>

                {/* Booking Form */}
                <motion.form
                    onSubmit={handleSubmit}
                    className="card booking-form"
                    initial={{ x: 20, opacity: 0 }}
                    animate={{ x: 0, opacity: 1 }}
                    transition={{ delay: 0.3 }}
                >
                    {error && (
                        <motion.div
                            className="error-message"
                            initial={{ height: 0, opacity: 0 }}
                            animate={{ height: 'auto', opacity: 1 }}
                        >
                            <FiAlertCircle /> {error}
                        </motion.div>
                    )}

                    <div className="form-group">
                        <label>Date</label>
                        <div className="input-with-icon" style={{ position: 'relative' }}>
                            <FiCalendar style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-secondary)' }} />
                            <input
                                type="date"
                                name="bookingDate"
                                value={bookingData.bookingDate}
                                onChange={handleChange}
                                onBlur={checkAvailability}
                                required
                                min={new Date().toISOString().split('T')[0]}
                                style={{ paddingLeft: '3rem' }}
                            />
                        </div>
                    </div>

                    <div className="form-row" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                        <div className="form-group">
                            <label>Start Time</label>
                            <input
                                type="time"
                                name="startTime"
                                value={bookingData.startTime}
                                onChange={handleChange}
                                onBlur={checkAvailability}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>End Time</label>
                            <input
                                type="time"
                                name="endTime"
                                value={bookingData.endTime}
                                onChange={handleChange}
                                onBlur={checkAvailability}
                                required
                            />
                        </div>
                    </div>

                    {availability && (
                        <motion.div
                            className={`availability-status ${availability.available ? 'available' : 'unavailable'}`}
                            initial={{ opacity: 0, scale: 0.9 }}
                            animate={{ opacity: 1, scale: 1 }}
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                gap: '0.5rem',
                                background: availability.available ? 'rgba(34, 197, 94, 0.1)' : 'rgba(239, 68, 68, 0.1)',
                                color: availability.available ? 'var(--success)' : 'var(--danger)',
                                padding: '1rem',
                                borderRadius: 'var(--radius-md)',
                                marginBottom: '1rem'
                            }}
                        >
                            {availability.available ? (
                                <><FiCheckCircle /> {availability.availableSlots} slots available</>
                            ) : (
                                <><FiAlertCircle /> No slots available for selected time</>
                            )}
                        </motion.div>
                    )}

                    {price && (
                        <motion.div
                            className="price-display"
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            style={{
                                textAlign: 'center',
                                padding: '1rem',
                                background: 'var(--bg-primary)',
                                borderRadius: 'var(--radius-md)',
                                marginBottom: '1.5rem',
                                border: '1px solid var(--primary)'
                            }}
                        >
                            <span style={{ color: 'var(--text-secondary)' }}>Total Price</span>
                            <div style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--primary)' }}>₹{price}</div>
                        </motion.div>
                    )}

                    <motion.button
                        type="submit"
                        className="btn btn-primary"
                        style={{ width: '100%', justifyContent: 'center' }}
                        disabled={loading || !availability?.available}
                        whileHover={{ scale: 1.02 }}
                        whileTap={{ scale: 0.98 }}
                    >
                        {loading ? 'Processing...' : 'Confirm Booking'}
                    </motion.button>
                </motion.form>
            </div>

            <style jsx>{`
                @media (max-width: 768px) {
                    .booking-layout {
                        grid-template-columns: 1fr !important;
                    }
                }
            `}</style>
        </motion.div>
    );
};

export default BookingPage;
