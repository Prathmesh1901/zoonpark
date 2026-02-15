# Database Setup Guide

Complete guide to set up PostgreSQL database for the Parking Booking System.

## Prerequisites

- PostgreSQL 14 or higher installed
- PostgreSQL service running
- Access to PostgreSQL command line (psql) or pgAdmin

## Method 1: Using Command Line (psql)

### Step 1: Open PostgreSQL Command Line

**Windows**:
```bash
# Open Command Prompt or PowerShell
# Navigate to PostgreSQL bin directory (adjust version as needed)
cd "C:\Program Files\PostgreSQL\14\bin"

# Connect to PostgreSQL as superuser
psql -U postgres
```

**Linux/Mac**:
```bash
sudo -u postgres psql
```

### Step 2: Run Setup Script

```sql
-- Run the setup script
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/setup.sql'
```

Or copy-paste the contents of `setup.sql` into the psql prompt.

### Step 3: (Optional) Load Sample Data

```sql
-- Run the sample data script
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/sample-data.sql'
```

### Step 4: Verify Setup

```sql
-- List databases
\l

-- Connect to the database
\c parking_booking_db

-- List tables
\dt

-- Check roles
SELECT * FROM roles;

-- Check if admin user exists
SELECT email, full_name FROM users WHERE email = 'admin@parkingbook.com';
```

---

## Method 2: Using pgAdmin (GUI)

### Step 1: Open pgAdmin

1. Launch pgAdmin 4
2. Connect to your PostgreSQL server
3. Enter your postgres password

### Step 2: Create Database

1. Right-click on "Databases"
2. Select "Create" → "Database"
3. Name: `parking_booking_db`
4. Owner: `postgres`
5. Click "Save"

### Step 3: Run SQL Scripts

1. Right-click on `parking_booking_db`
2. Select "Query Tool"
3. Open `setup.sql` file
4. Click "Execute" (F5)
5. (Optional) Open and execute `sample-data.sql`

### Step 4: Verify

1. Right-click on `parking_booking_db`
2. Select "Refresh"
3. Expand "Schemas" → "public" → "Tables"
4. You should see: roles, users, parking_spaces, bookings

---

## Method 3: Let Hibernate Create Tables (Easiest)

If you don't want to run SQL scripts manually, Hibernate can create tables automatically.

### Step 1: Create Empty Database

```sql
-- In psql or pgAdmin
CREATE DATABASE parking_booking_db;
```

### Step 2: Update application.properties

The application is already configured with:
```properties
spring.jpa.hibernate.ddl-auto=update
```

This means Hibernate will:
- Create tables if they don't exist
- Update schema if entities change
- NOT drop existing data

### Step 3: Start Spring Boot Application

```bash
cd backend
mvn spring-boot:run
```

Hibernate will automatically:
1. Create all tables based on entity classes
2. Create indexes
3. Insert default roles (via DataInitializer.java)

---

## Database Configuration

### Default Credentials

Update these in `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_booking_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

**Change the password** if your PostgreSQL has a different password!

### Connection String Format

```
jdbc:postgresql://[host]:[port]/[database]
```

Example:
- Local: `jdbc:postgresql://localhost:5432/parking_booking_db`
- Remote: `jdbc:postgresql://192.168.1.100:5432/parking_booking_db`

---

## Default Users (After Sample Data)

### Admin Account
- **Email**: admin@parkingbook.com
- **Password**: admin123
- **Role**: ADMIN

### Test Customer Accounts
- **Email**: john@example.com
- **Password**: password123
- **Role**: CUSTOMER

- **Email**: jane@example.com
- **Password**: password123
- **Role**: CUSTOMER

### Test Owner Accounts
- **Email**: owner1@example.com
- **Password**: password123
- **Role**: OWNER (Approved)

- **Email**: owner2@example.com
- **Password**: password123
- **Role**: OWNER (Approved)

- **Email**: owner3@example.com
- **Password**: password123
- **Role**: OWNER (Pending Approval)

---

## Database Schema

### Tables Created

1. **roles** - User roles (CUSTOMER, OWNER, ADMIN)
2. **users** - All user accounts
3. **parking_spaces** - Parking locations
4. **bookings** - Reservations

### Indexes Created

- `idx_users_email` - Fast login lookup
- `idx_users_role` - Filter by role
- `idx_parking_city_area` - Search by location
- `idx_parking_owner` - Owner's parking spaces
- `idx_booking_availability` - **Most important!** Fast availability checks
- `idx_booking_id` - Quick booking lookup
- `idx_booking_user` - User's bookings

---

## Troubleshooting

### Issue 1: "database already exists"

**Solution**: Drop and recreate
```sql
DROP DATABASE IF EXISTS parking_booking_db;
CREATE DATABASE parking_booking_db;
```

### Issue 2: "permission denied"

**Solution**: Grant permissions
```sql
GRANT ALL PRIVILEGES ON DATABASE parking_booking_db TO postgres;
```

### Issue 3: "connection refused"

**Solution**: Check if PostgreSQL is running
```bash
# Windows
services.msc
# Look for "postgresql-x64-14" service

# Linux
sudo systemctl status postgresql
```

### Issue 4: "password authentication failed"

**Solution**: 
1. Check your PostgreSQL password
2. Update `application.properties` with correct password
3. Or reset PostgreSQL password

### Issue 5: Can't connect from Spring Boot

**Solution**: Check `pg_hba.conf`
```
# Location: C:\Program Files\PostgreSQL\14\data\pg_hba.conf
# Add this line:
host    all             all             127.0.0.1/32            md5
```

Then restart PostgreSQL service.

---

## Useful SQL Commands

### View All Data

```sql
-- Connect to database
\c parking_booking_db

-- View roles
SELECT * FROM roles;

-- View users
SELECT u.id, u.email, u.full_name, r.name as role, u.is_approved 
FROM users u 
JOIN roles r ON u.role_id = r.id;

-- View parking spaces
SELECT id, name, city, area, total_slots, price_per_hour, is_active 
FROM parking_spaces;

-- View bookings
SELECT b.booking_id, u.full_name as user, p.name as parking, 
       b.booking_date, b.start_time, b.end_time, b.status
FROM bookings b
JOIN users u ON b.user_id = u.id
JOIN parking_spaces p ON b.parking_space_id = p.id;
```

### Reset Database

```sql
-- Delete all data (keeps tables)
TRUNCATE TABLE bookings CASCADE;
TRUNCATE TABLE parking_spaces CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE roles CASCADE;

-- Then re-run setup.sql to insert roles
```

### Backup Database

```bash
# Backup
pg_dump -U postgres parking_booking_db > backup.sql

# Restore
psql -U postgres parking_booking_db < backup.sql
```

---

## Next Steps

After database setup:

1. ✅ Database is ready
2. ▶️ Start backend: `cd backend && mvn spring-boot:run`
3. ▶️ Start frontend: `cd frontend && npm install && npm run dev`
4. 🌐 Open browser: `http://localhost:5173`

---

## Database ER Diagram

```
┌─────────────┐
│    roles    │
│─────────────│
│ id (PK)     │
│ name        │
│ description │
└─────────────┘
       ▲
       │
       │ role_id (FK)
       │
┌─────────────┐         ┌──────────────────┐
│    users    │◄────────│ parking_spaces   │
│─────────────│         │──────────────────│
│ id (PK)     │         │ id (PK)          │
│ email       │         │ owner_id (FK)    │
│ password    │         │ name             │
│ full_name   │         │ city             │
│ phone       │         │ area             │
│ role_id(FK) │         │ total_slots      │
│ is_approved │         │ price_per_hour   │
└─────────────┘         └──────────────────┘
       ▲                         ▲
       │                         │
       │ user_id (FK)            │ parking_space_id (FK)
       │                         │
       │    ┌──────────────┐    │
       └────│   bookings   │────┘
            │──────────────│
            │ id (PK)      │
            │ user_id (FK) │
            │ parking_id   │
            │ booking_id   │
            │ booking_date │
            │ start_time   │
            │ end_time     │
            │ total_price  │
            │ status       │
            └──────────────┘
```

---

## Support

If you encounter any issues:
1. Check PostgreSQL service is running
2. Verify credentials in `application.properties`
3. Check PostgreSQL logs
4. Ensure port 5432 is not blocked by firewall

Good luck! 🚀
