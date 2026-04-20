-- ============================================
-- Sample Data for Testing
-- ============================================
-- This script inserts sample data for testing the application
-- Run this AFTER the main setup.sql script

\c parking_booking_db;

-- Sample Customers (password for all: password123)
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO
INSERT INTO users (email, password, full_name, phone, role_id, is_approved) VALUES
('john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'John Doe', '9876543210', (SELECT id FROM roles WHERE name = 'CUSTOMER'), TRUE),
('jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Jane Smith', '9876543211', (SELECT id FROM roles WHERE name = 'CUSTOMER'), TRUE),
('mike@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Mike Johnson', '9876543212', (SELECT id FROM roles WHERE name = 'CUSTOMER'), TRUE)
ON CONFLICT (email) DO NOTHING;

-- Sample Parking Owners (password: password123)
INSERT INTO users (email, password, full_name, phone, role_id, is_approved) VALUES
('owner1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Parking Owner 1', '9876543220', (SELECT id FROM roles WHERE name = 'OWNER'), TRUE),
('owner2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Parking Owner 2', '9876543221', (SELECT id FROM roles WHERE name = 'OWNER'), TRUE),
('owner3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Parking Owner 3', '9876543222', (SELECT id FROM roles WHERE name = 'OWNER'), FALSE)
ON CONFLICT (email) DO NOTHING;

-- Sample Parking Spaces
INSERT INTO parking_spaces (owner_id, name, city, area, address, total_slots, price_per_hour, opening_time, closing_time, is_active) VALUES
((SELECT id FROM users WHERE email = 'owner1@example.com'), 'City Center Parking', 'Mumbai', 'Andheri', '123 Main Street, Andheri West, Mumbai', 50, 50.00, '06:00:00', '23:00:00', TRUE),
((SELECT id FROM users WHERE email = 'owner1@example.com'), 'Mall Parking Plaza', 'Mumbai', 'Bandra', '456 Linking Road, Bandra West, Mumbai', 100, 60.00, '08:00:00', '22:00:00', TRUE),
((SELECT id FROM users WHERE email = 'owner2@example.com'), 'Airport Parking', 'Mumbai', 'Andheri', 'Near Domestic Terminal, Andheri East, Mumbai', 200, 80.00, '00:00:00', '23:59:59', TRUE),
((SELECT id FROM users WHERE email = 'owner2@example.com'), 'Station Parking', 'Mumbai', 'Dadar', 'Opposite Dadar Railway Station, Mumbai', 75, 40.00, '05:00:00', '23:00:00', TRUE),
((SELECT id FROM users WHERE email = 'owner1@example.com'), 'Beach Side Parking', 'Mumbai', 'Juhu', 'Juhu Beach Road, Mumbai', 30, 70.00, '06:00:00', '22:00:00', TRUE),
((SELECT id FROM users WHERE email = 'owner2@example.com'), 'Tech Park Parking', 'Bangalore', 'Whitefield', 'ITPL Main Road, Whitefield, Bangalore', 150, 45.00, '07:00:00', '21:00:00', TRUE),
((SELECT id FROM users WHERE email = 'owner2@example.com'), 'Metro Parking', 'Delhi', 'Connaught Place', 'Near Rajiv Chowk Metro, CP, Delhi', 80, 55.00, '06:00:00', '23:00:00', TRUE);

-- Sample Bookings
INSERT INTO bookings (user_id, parking_space_id, booking_id, booking_date, start_time, end_time, total_price, status) VALUES
((SELECT id FROM users WHERE email = 'john@example.com'), 
 (SELECT id FROM parking_spaces WHERE name = 'City Center Parking'), 
 'BK20260129001', '2026-01-30', '10:00:00', '14:00:00', 200.00, 'CONFIRMED'),

((SELECT id FROM users WHERE email = 'jane@example.com'), 
 (SELECT id FROM parking_spaces WHERE name = 'Mall Parking Plaza'), 
 'BK20260129002', '2026-01-30', '09:00:00', '12:00:00', 180.00, 'CONFIRMED'),

((SELECT id FROM users WHERE email = 'mike@example.com'), 
 (SELECT id FROM parking_spaces WHERE name = 'Airport Parking'), 
 'BK20260129003', '2026-01-31', '08:00:00', '20:00:00', 960.00, 'CONFIRMED'),

((SELECT id FROM users WHERE email = 'john@example.com'), 
 (SELECT id FROM parking_spaces WHERE name = 'Beach Side Parking'), 
 'BK20260129004', '2026-02-01', '15:00:00', '18:00:00', 210.00, 'CONFIRMED'),

((SELECT id FROM users WHERE email = 'jane@example.com'), 
 (SELECT id FROM parking_spaces WHERE name = 'City Center Parking'), 
 'BK20260129005', '2026-01-28', '10:00:00', '13:00:00', 150.00, 'COMPLETED');

-- Verify the data
SELECT 'Roles Count: ' || COUNT(*) FROM roles;
SELECT 'Users Count: ' || COUNT(*) FROM users;
SELECT 'Parking Spaces Count: ' || COUNT(*) FROM parking_spaces;
SELECT 'Bookings Count: ' || COUNT(*) FROM bookings;

-- Show sample data
SELECT '=== Sample Users ===' AS info;
SELECT email, full_name, r.name as role, is_approved FROM users u JOIN roles r ON u.role_id = r.id;

SELECT '=== Sample Parking Spaces ===' AS info;
SELECT name, city, area, total_slots, price_per_hour FROM parking_spaces LIMIT 5;

SELECT '=== Sample Bookings ===' AS info;
SELECT booking_id, booking_date, start_time, end_time, total_price, status FROM bookings LIMIT 5;
