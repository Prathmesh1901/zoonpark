import React, { useState, useEffect } from 'react';
import bookingService from '../../services/bookingService';

/**
 * UserDashboard Component
 * 
 * Shows user's booking history
 * Allows users to cancel bookings
 */
const UserDashboard = () => {
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        loadBookings();
    }, []);

    const loadBookings = async () => {
        try {
            const data = await bookingService.getMyBookings();
            setBookings(data);
        } catch (err) {
            setError('Failed to load bookings');
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (bookingId) => {
        if (!window.confirm('Are you sure you want to cancel this booking?')) {
            return;
        }

        try {
            await bookingService.cancelBooking(bookingId);
            alert('Booking cancelled successfully');
            loadBookings();
        } catch (err) {
            alert('Failed to cancel booking');
        }
    };

    if (loading) return <p>Loading...</p>;

    return (
        <div className="dashboard-container">
            <h1>My Bookings</h1>

            {error && <div className="error-message">{error}</div>}

            {bookings.length === 0 ? (
                <p>No bookings yet</p>
            ) : (
                <div className="bookings-list">
                    {bookings.map((booking) => (
                        <div key={booking.id} className="booking-card">
                            <h3>{booking.parkingSpaceName}</h3>
                            <p>📍 {booking.parkingLocation}</p>
                            <p>📅 {booking.bookingDate}</p>
                            <p>🕒 {booking.startTime} - {booking.endTime}</p>
                            <p>💰 ₹{booking.totalPrice}</p>
                            <p className={`status status-${booking.status.toLowerCase()}`}>
                                {booking.status}
                            </p>
                            <p className="booking-id">Booking ID: {booking.bookingId}</p>

                            {booking.status === 'CONFIRMED' && (
                                <button
                                    onClick={() => handleCancel(booking.bookingId)}
                                    className="btn btn-danger"
                                    style={{ marginTop: '1rem' }}
                                >
                                    Cancel Booking
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default UserDashboard;
