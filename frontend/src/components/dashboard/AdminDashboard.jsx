import React, { useState, useEffect } from 'react';
import api from '../../services/api';

/**
 * AdminDashboard Component
 * 
 * Shows admin statistics and pending owner approvals
 */
const AdminDashboard = () => {
    const [stats, setStats] = useState(null);
    const [pendingOwners, setPendingOwners] = useState([]);

    useEffect(() => {
        loadDashboardData();
    }, []);

    const loadDashboardData = async () => {
        try {
            const statsData = await api.get('/admin/dashboard');
            setStats(statsData.data);

            const ownersData = await api.get('/admin/owners/pending');
            setPendingOwners(ownersData.data);
        } catch (err) {
            alert('Failed to load dashboard data');
        }
    };

    const handleApprove = async (id) => {
        try {
            await api.put(`/admin/owners/${id}/approve`);
            alert('Owner approved successfully');
            loadDashboardData();
        } catch (err) {
            alert('Failed to approve owner');
        }
    };

    const handleBlock = async (id) => {
        try {
            await api.put(`/admin/owners/${id}/block`);
            alert('Owner blocked successfully');
            loadDashboardData();
        } catch (err) {
            alert('Failed to block owner');
        }
    };

    if (!stats) return <p>Loading...</p>;

    return (
        <div className="dashboard-container">
            <h1>Admin Dashboard</h1>

            <div className="stats-grid">
                <div className="stat-card">
                    <h3>Total Users</h3>
                    <p className="stat-number">{stats.totalUsers}</p>
                </div>
                <div className="stat-card">
                    <h3>Customers</h3>
                    <p className="stat-number">{stats.totalCustomers}</p>
                </div>
                <div className="stat-card">
                    <h3>Owners</h3>
                    <p className="stat-number">{stats.totalOwners}</p>
                </div>
                <div className="stat-card">
                    <h3>Parking Spaces</h3>
                    <p className="stat-number">{stats.totalParkingSpaces}</p>
                </div>
                <div className="stat-card">
                    <h3>Total Bookings</h3>
                    <p className="stat-number">{stats.totalBookings}</p>
                </div>
                <div className="stat-card">
                    <h3>Confirmed Bookings</h3>
                    <p className="stat-number">{stats.confirmedBookings}</p>
                </div>
            </div>

            <h2>Pending Owner Approvals</h2>
            {pendingOwners.length === 0 ? (
                <p>No pending approvals</p>
            ) : (
                <div className="owners-list">
                    {pendingOwners.map((owner) => (
                        <div key={owner.id} className="owner-card">
                            <h3>{owner.fullName}</h3>
                            <p>📧 {owner.email}</p>
                            <p>📱 {owner.phone}</p>
                            <div className="owner-actions">
                                <button onClick={() => handleApprove(owner.id)} className="btn-primary">
                                    Approve
                                </button>
                                <button onClick={() => handleBlock(owner.id)} className="btn-danger">
                                    Reject
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default AdminDashboard;
