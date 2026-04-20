-- ============================================
-- Parking Booking System - Database Setup
-- ============================================
-- This script creates the database and initial data
-- Run this script as PostgreSQL superuser (postgres)

-- 1. Create Database
-- ============================================
-- Drop database if exists (CAUTION: This deletes all data!)
DROP DATABASE IF EXISTS parking_booking_db;

-- Create fresh database
CREATE DATABASE parking_booking_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Connect to the database
\c parking_booking_db;

-- 2. Create Tables
-- ============================================
-- Note: Hibernate will auto-create tables based on entities
-- But you can manually create them if needed

-- Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role_id BIGINT NOT NULL,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Parking Spaces Table
CREATE TABLE IF NOT EXISTS parking_spaces (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    area VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    total_slots INTEGER NOT NULL,
    price_per_hour DECIMAL(10, 2) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    parking_space_id BIGINT NOT NULL,
    booking_id VARCHAR(50) UNIQUE NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_parking_space FOREIGN KEY (parking_space_id) REFERENCES parking_spaces(id)
);

-- 3. Create Indexes for Performance
-- ============================================

-- Index on email for fast login
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Index on role for filtering
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role_id);

-- Index on city and area for search
CREATE INDEX IF NOT EXISTS idx_parking_city_area ON parking_spaces(city, area);

-- Index on owner for owner's parking spaces
CREATE INDEX IF NOT EXISTS idx_parking_owner ON parking_spaces(owner_id);

-- Composite index for availability queries (most important!)
CREATE INDEX IF NOT EXISTS idx_booking_availability 
ON bookings(parking_space_id, booking_date, status);

-- Index on booking_id for quick lookup
CREATE INDEX IF NOT EXISTS idx_booking_id ON bookings(booking_id);

-- Index on user for user's bookings
CREATE INDEX IF NOT EXISTS idx_booking_user ON bookings(user_id);

-- 4. Insert Initial Data
-- ============================================

-- Insert Roles
INSERT INTO roles (name, description) VALUES
('CUSTOMER', 'Regular user who books parking spaces'),
('OWNER', 'Parking space owner who provides parking'),
('ADMIN', 'Administrator who manages the platform')
ON CONFLICT (name) DO NOTHING;

-- Insert Admin User (password: admin123)
-- Note: This is BCrypt hash of 'admin123'
INSERT INTO users (email, password, full_name, phone, role_id, is_approved)
SELECT 
    'admin@parkingbook.com',
    '$2a$10$xQKXvVvVvVvVvVvVvVvVvOeKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK',
    'System Admin',
    '9999999999',
    (SELECT id FROM roles WHERE name = 'ADMIN'),
    TRUE
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@parkingbook.com'
);

-- 5. Grant Permissions
-- ============================================
-- Grant all privileges to postgres user
GRANT ALL PRIVILEGES ON DATABASE parking_booking_db TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- ============================================
-- Setup Complete!
-- ============================================
-- You can now start the Spring Boot application
-- Hibernate will validate the schema and create any missing tables

-- To verify the setup, run:
-- SELECT * FROM roles;
-- SELECT * FROM users;
