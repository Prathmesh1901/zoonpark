# 🚀 Final Setup Steps - You're Almost There!

Your PostgreSQL 18 is installed and ready. Follow these simple steps to complete the setup:

## What You Need

**Your PostgreSQL Password**: The password you set during PostgreSQL installation.

The application is configured to use:
- **Username**: `postgres`
- **Password**: `postgres` (change in `application.properties` if yours is different)

---

## Quick Setup (Choose ONE method)

### Method 1: Automated Script (Easiest!) ⭐

1. **Double-click** this file:
   ```
   database\create-database.bat
   ```

2. Enter your PostgreSQL password when prompted

3. When asked "Load sample data?", type `Y` for yes (recommended for testing)

4. Done! The script will:
   - Create the database
   - Create all tables
   - Load sample test data (7 parking spaces, 6 users, 5 bookings)

---

### Method 2: Using pgAdmin (GUI)

1. Open **pgAdmin 4** from Start menu

2. Expand "Servers" → "PostgreSQL 18"

3. Enter your postgres password

4. Right-click "Databases" → "Create" → "Database"
   - Name: `parking_booking_db`
   - Click "Save"

5. Right-click `parking_booking_db` → "Query Tool"

6. Click "Open File" icon, select:
   ```
   C:\Users\acer\.gemini\antigravity\scratch\parking-booking-app\database\setup.sql
   ```

7. Click "Execute" (▶️ button or F5)

8. (Optional) Repeat for `sample-data.sql` to load test data

---

### Method 3: Let Hibernate Do It (Simplest!)

1. Open pgAdmin or use psql to create empty database:
   ```sql
   CREATE DATABASE parking_booking_db;
   ```

2. That's it! When you start Spring Boot, Hibernate will create all tables automatically.

---

## Update Password (If Needed)

If your PostgreSQL password is NOT `postgres`, update this file:

**File**: `backend\src\main\resources\application.properties`

**Line 4**: Change `postgres` to your actual password:
```properties
spring.datasource.password=YOUR_PASSWORD_HERE
```

---

## Verify Database Setup

After running setup, verify in pgAdmin or psql:

```sql
-- Connect to database
\c parking_booking_db

-- List tables (should see 4 tables)
\dt

-- Check roles (should see 3 roles)
SELECT * FROM roles;

-- Check sample users (if you loaded sample data)
SELECT email, full_name FROM users;
```

You should see:
- ✅ 4 tables: roles, users, parking_spaces, bookings
- ✅ 3 roles: CUSTOMER, OWNER, ADMIN
- ✅ Sample users (if loaded): admin@parkingbook.com, john@example.com, etc.

---

## Test Accounts (After Loading Sample Data)

| Email | Password | Role |
|-------|----------|------|
| admin@parkingbook.com | admin123 | ADMIN |
| john@example.com | password123 | CUSTOMER |
| jane@example.com | password123 | CUSTOMER |
| owner1@example.com | password123 | OWNER (Approved) |
| owner2@example.com | password123 | OWNER (Approved) |
| owner3@example.com | password123 | OWNER (Pending) |

---

## Next: Start the Application! 🎉

Once database is ready:

### 1. Start Backend

```bash
cd backend
mvn spring-boot:run
```

Wait for: `Started ParkingBookingApplication in X seconds`

Backend will run on: `http://localhost:8080`

### 2. Start Frontend (New Terminal)

```bash
cd frontend
npm install
npm run dev
```

Frontend will run on: `http://localhost:5173`

### 3. Open Browser

Navigate to: `http://localhost:5173`

You should see the Parking Booking System home page!

---

## Troubleshooting

### "Database already exists"
- **Solution**: That's OK! Just run the `setup.sql` script on the existing database

### "Password authentication failed"
- **Solution**: Update the password in `application.properties` to match your PostgreSQL password

### "Port 8080 already in use"
- **Solution**: Another application is using port 8080. Stop it or change the port in `application.properties`

### Backend won't start
- **Solution**: 
  1. Check database is created
  2. Check password in `application.properties`
  3. Check PostgreSQL service is running

---

## Ready to Go! 🚀

After database setup:
1. ✅ Database created
2. ▶️ Start backend: `cd backend && mvn spring-boot:run`
3. ▶️ Start frontend: `cd frontend && npm install && npm run dev`
4. 🌐 Open: `http://localhost:5173`

**Let me know when you're ready to start the application!**
