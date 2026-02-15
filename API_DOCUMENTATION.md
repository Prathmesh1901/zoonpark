# API Documentation - Parking Booking System

Complete API reference with request/response examples for all endpoints.

## Base URL
```
http://localhost:8080/api
```

## Authentication

All protected endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 1. Authentication APIs

### 1.1 Register User

**Endpoint**: `POST /auth/register`

**Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phone": "9876543210",
  "role": "CUSTOMER"
}
```

**Response** (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA2NTI3MjAwLCJleHAiOjE3MDY2MTM2MDB9.signature",
  "type": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "fullName": "John Doe",
  "role": "CUSTOMER",
  "isApproved": true
}
```

### 1.2 Login

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "john@example.com",
  "fullName": "John Doe",
  "role": "CUSTOMER",
  "isApproved": true
}
```

---

## 2. Parking Space APIs

### 2.1 Search Parking Spaces

**Endpoint**: `GET /parking-spaces?city=Mumbai&area=Andheri`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "City Center Parking",
    "city": "Mumbai",
    "area": "Andheri",
    "address": "123 Main Street, Andheri, Mumbai",
    "totalSlots": 50,
    "pricePerHour": 50.00,
    "openingTime": "06:00:00",
    "closingTime": "23:00:00",
    "isActive": true,
    "owner": {
      "id": 2,
      "fullName": "Owner Name"
    }
  }
]
```

### 2.2 Create Parking Space (OWNER only)

**Endpoint**: `POST /parking-spaces`

**Headers**: `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "name": "City Center Parking",
  "city": "Mumbai",
  "area": "Andheri",
  "address": "123 Main Street, Andheri, Mumbai",
  "totalSlots": 50,
  "pricePerHour": 50.00,
  "openingTime": "06:00",
  "closingTime": "23:00"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "name": "City Center Parking",
  "city": "Mumbai",
  "area": "Andheri",
  "address": "123 Main Street, Andheri, Mumbai",
  "totalSlots": 50,
  "pricePerHour": 50.00,
  "openingTime": "06:00:00",
  "closingTime": "23:00:00",
  "isActive": true,
  "createdAt": "2026-01-29T17:30:00"
}
```

---

## 3. Booking APIs

### 3.1 Check Availability

**Endpoint**: `GET /bookings/check-availability?parkingSpaceId=1&date=2026-01-30&startTime=10:00&endTime=14:00`

**Response** (200 OK):
```json
{
  "available": true,
  "availableSlots": 5
}
```

### 3.2 Calculate Price

**Endpoint**: `GET /bookings/calculate-price?parkingSpaceId=1&startTime=10:00&endTime=14:00`

**Response** (200 OK):
```json
{
  "totalPrice": 200.00
}
```

### 3.3 Create Booking

**Endpoint**: `POST /bookings`

**Headers**: `Authorization: Bearer <token>`

**Request Body**:
```json
{
  "parkingSpaceId": 1,
  "bookingDate": "2026-01-30",
  "startTime": "10:00",
  "endTime": "14:00"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "bookingId": "BK20260129001",
  "userName": "John Doe",
  "userEmail": "john@example.com",
  "parkingSpaceName": "City Center Parking",
  "parkingLocation": "Mumbai, Andheri",
  "bookingDate": "2026-01-30",
  "startTime": "10:00:00",
  "endTime": "14:00:00",
  "totalPrice": 200.00,
  "status": "CONFIRMED",
  "createdAt": "2026-01-29T17:30:00"
}
```

### 3.4 Cancel Booking

**Endpoint**: `PUT /bookings/BK20260129001/cancel`

**Headers**: `Authorization: Bearer <token>`

**Response** (200 OK):
```json
{
  "message": "Booking cancelled successfully"
}
```

### 3.5 Get My Bookings

**Endpoint**: `GET /bookings/my-bookings`

**Headers**: `Authorization: Bearer <token>`

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "bookingId": "BK20260129001",
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "parkingSpaceName": "City Center Parking",
    "parkingLocation": "Mumbai, Andheri",
    "bookingDate": "2026-01-30",
    "startTime": "10:00:00",
    "endTime": "14:00:00",
    "totalPrice": 200.00,
    "status": "CONFIRMED",
    "createdAt": "2026-01-29T17:30:00"
  }
]
```

---

## 4. Admin APIs

### 4.1 Get Dashboard Statistics

**Endpoint**: `GET /admin/dashboard`

**Headers**: `Authorization: Bearer <token>` (ADMIN role required)

**Response** (200 OK):
```json
{
  "totalUsers": 100,
  "totalCustomers": 80,
  "totalOwners": 15,
  "totalAdmins": 5,
  "pendingOwners": 3,
  "totalParkingSpaces": 50,
  "activeParkingSpaces": 45,
  "totalBookings": 500,
  "confirmedBookings": 450,
  "cancelledBookings": 30,
  "completedBookings": 20
}
```

### 4.2 Get Pending Owners

**Endpoint**: `GET /admin/owners/pending`

**Headers**: `Authorization: Bearer <token>` (ADMIN role required)

**Response** (200 OK):
```json
[
  {
    "id": 5,
    "email": "owner@example.com",
    "fullName": "Parking Owner",
    "phone": "9876543210",
    "role": {
      "id": 2,
      "name": "OWNER"
    },
    "isApproved": false,
    "createdAt": "2026-01-29T17:00:00"
  }
]
```

### 4.3 Approve Owner

**Endpoint**: `PUT /admin/owners/5/approve`

**Headers**: `Authorization: Bearer <token>` (ADMIN role required)

**Response** (200 OK):
```json
{
  "message": "Owner approved successfully"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2026-01-29T17:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No slots available for the selected time"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2026-01-29T17:30:00",
  "status": 401,
  "error": "Authentication Failed",
  "message": "Invalid email or password"
}
```

### 404 Not Found
```json
{
  "timestamp": "2026-01-29T17:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Parking space not found"
}
```

### Validation Error (422)
```json
{
  "timestamp": "2026-01-29T17:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Email must be valid",
    "password": "Password must be at least 6 characters"
  }
}
```

---

## Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "phone": "9876543210",
    "role": "CUSTOMER"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Search Parking
```bash
curl -X GET "http://localhost:8080/api/parking-spaces?city=Mumbai&area=Andheri"
```

### Create Booking
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "parkingSpaceId": 1,
    "bookingDate": "2026-01-30",
    "startTime": "10:00",
    "endTime": "14:00"
  }'
```
