# Parking Space Booking System

A complete full-stack parking space booking application built with **Java Spring Boot** backend and **React** frontend. This project demonstrates a production-ready booking system similar to Zoomcar/ParkingHawker.

## 🚀 Features

### User Module
- User registration and login with JWT authentication
- Search parking spaces by location (city, area)
- Book parking spaces with date and time selection
- View booking history
- Cancel bookings

### Parking Owner Module
- Add and manage parking spaces
- Set pricing and operating hours
- View bookings for owned spaces
- Admin approval required before listing

### Admin Module
- Approve/block parking owners
- View all users, bookings, and parking spaces
- Dashboard with statistics

### Core Functionality
- **Smart Availability Calculation**: No hardware sensors needed! Availability is calculated using:
  ```
  Available Slots = Total Slots - Overlapping Bookings
  ```
- **Dynamic Pricing**: Automatic price calculation based on hours
- **Time Slot Management**: Prevents double booking through backend validation

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.1
- Spring Security (JWT Authentication)
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

### Frontend
- React 18
- Vite
- React Router DOM
- Axios
- Vanilla CSS

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **PostgreSQL 14** or higher
- **Maven 3.8** or higher

## 🔧 Setup Instructions

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE parking_booking_db;

-- Connect to database
\c parking_booking_db;

-- Tables will be created automatically by Hibernate
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Update database credentials in src/main/resources/application.properties
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will start on `http://localhost:5173`

## 📱 Usage

### 1. Register as Customer
1. Go to `http://localhost:5173`
2. Click "Register"
3. Fill in details and select "Customer" role
4. Login with your credentials

### 2. Search and Book Parking
1. Go to "Search Parking"
2. Enter city and area (e.g., Mumbai, Andheri)
3. Click on a parking space
4. Select date and time
5. System checks availability automatically
6. Confirm booking

### 3. Register as Owner
1. Register with "Owner" role
2. Wait for admin approval
3. After approval, add parking spaces
4. Set slots, price, and operating hours

### 4. Admin Access
1. Register with "ADMIN" role (or create via database)
2. Access admin dashboard
3. Approve pending owners
4. View statistics

## 🔐 Default Roles

The system creates three roles automatically:
- **CUSTOMER**: Can search and book parking spaces
- **OWNER**: Can add and manage parking spaces (requires admin approval)
- **ADMIN**: Can manage users and view statistics

## 📊 Database Schema

```
users
├── id (PK)
├── email (unique)
├── password (encrypted)
├── full_name
├── phone
├── role_id (FK)
└── is_approved

roles
├── id (PK)
├── name (unique)
└── description

parking_spaces
├── id (PK)
├── owner_id (FK)
├── name
├── city
├── area
├── address
├── total_slots
├── price_per_hour
├── opening_time
├── closing_time
└── is_active

bookings
├── id (PK)
├── user_id (FK)
├── parking_space_id (FK)
├── booking_id (unique)
├── booking_date
├── start_time
├── end_time
├── total_price
└── status
```

## 🌐 API Endpoints

### Authentication
- `POST /api/auth/register` - Register user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user

### Parking Spaces
- `GET /api/parking-spaces` - Search parking spaces
- `POST /api/parking-spaces` - Create parking space (OWNER)
- `GET /api/parking-spaces/{id}` - Get parking space details
- `PUT /api/parking-spaces/{id}` - Update parking space (OWNER)
- `DELETE /api/parking-spaces/{id}` - Delete parking space (OWNER)

### Bookings
- `GET /api/bookings/check-availability` - Check slot availability
- `GET /api/bookings/calculate-price` - Calculate booking price
- `POST /api/bookings` - Create booking
- `GET /api/bookings/{id}` - Get booking details
- `PUT /api/bookings/{id}/cancel` - Cancel booking
- `GET /api/bookings/my-bookings` - Get user's bookings

### Admin
- `GET /api/admin/users` - Get all users
- `GET /api/admin/owners/pending` - Get pending owners
- `PUT /api/admin/owners/{id}/approve` - Approve owner
- `GET /api/admin/dashboard` - Get statistics

## 🎯 Key Concepts for Interviews

### 1. Availability Logic (Most Important!)
**Question**: How do you manage parking availability without sensors?

**Answer**: 
```java
// Count overlapping bookings for the same parking space, date, and time
Long overlappingBookings = bookingRepository.countOverlappingBookings(
    parkingSpaceId, date, startTime, endTime
);

// Calculate available slots
int availableSlots = totalSlots - overlappingBookings;

// Two bookings overlap if:
// (new_start < existing_end) AND (new_end > existing_start)
```

### 2. JWT Authentication
**Question**: How does JWT authentication work?

**Answer**:
1. User logs in with email/password
2. Server validates credentials
3. Server generates JWT token containing user info
4. Client stores token in localStorage
5. Client sends token in Authorization header for each request
6. Server validates token and extracts user info

### 3. Spring Security Configuration
**Question**: How did you implement role-based access control?

**Answer**:
- Used Spring Security with JWT
- Created custom UserDetailsService to load users
- Configured SecurityFilterChain with role-based rules
- CUSTOMER, OWNER, ADMIN roles with different permissions

### 4. React State Management
**Question**: How did you manage authentication state in React?

**Answer**:
- Used React Context API (AuthContext)
- Stored user and token in localStorage
- Created custom useAuth hook for easy access
- Protected routes using ProtectedRoute component

## 🐛 Common Issues and Fixes

### Issue 1: CORS Error
**Problem**: Frontend can't call backend APIs

**Fix**: 
- Check SecurityConfig.java has CORS configuration
- Verify frontend origin is allowed
- Check Vite proxy configuration

### Issue 2: JWT Token Not Working
**Problem**: Getting 401 Unauthorized

**Fix**:
- Check token is stored in localStorage
- Verify Authorization header format: `Bearer <token>`
- Check token hasn't expired (24 hours default)

### Issue 3: Double Booking
**Problem**: Multiple users booking same slot

**Fix**:
- Use @Transactional on booking creation
- Implement database-level validation
- Consider optimistic locking for high concurrency

## 📈 Future Enhancements

- Payment gateway integration
- Email notifications
- QR code for booking verification
- Reviews and ratings
- Mobile app (React Native)
- Real-time availability updates (WebSocket)

## 👨‍💻 Author

Built as a comprehensive full-stack project for fresher interviews and resume.

## 📄 License

This project is open source and available for learning purposes.
