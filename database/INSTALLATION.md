# PostgreSQL Installation Guide for Windows

PostgreSQL is not currently installed or not in your system PATH. Follow this guide to install it.

## Option 1: Install PostgreSQL (Recommended)

### Step 1: Download PostgreSQL

1. Go to: https://www.postgresql.org/download/windows/
2. Click "Download the installer"
3. Download PostgreSQL 16 (or latest version) for Windows x86-64

**Direct Link**: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

### Step 2: Run the Installer

1. Run the downloaded `.exe` file
2. Click "Next" through the welcome screen
3. **Installation Directory**: Keep default `C:\Program Files\PostgreSQL\16`
4. **Select Components**: Check all (PostgreSQL Server, pgAdmin 4, Stack Builder, Command Line Tools)
5. **Data Directory**: Keep default
6. **Password**: Set a password for the postgres superuser (REMEMBER THIS!)
   - Example: `postgres` (simple for development)
7. **Port**: Keep default `5432`
8. **Locale**: Keep default
9. Click "Next" and then "Install"
10. Wait for installation to complete
11. Uncheck "Stack Builder" at the end (not needed)
12. Click "Finish"

### Step 3: Add PostgreSQL to PATH (Important!)

1. Open "Environment Variables":
   - Press `Win + X`
   - Select "System"
   - Click "Advanced system settings"
   - Click "Environment Variables"

2. Under "System variables", find "Path"
3. Click "Edit"
4. Click "New"
5. Add: `C:\Program Files\PostgreSQL\16\bin`
6. Click "OK" on all windows

7. **Restart your terminal/PowerShell** for changes to take effect

### Step 4: Verify Installation

Open a new PowerShell window and run:
```powershell
psql --version
```

You should see: `psql (PostgreSQL) 16.x`

---

## Option 2: Use Docker (Alternative)

If you prefer Docker:

```bash
# Pull PostgreSQL image
docker pull postgres:16

# Run PostgreSQL container
docker run --name parking-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16

# Verify it's running
docker ps
```

---

## After Installation: Set Up Database

### Method 1: Using pgAdmin (GUI - Easiest)

1. Open **pgAdmin 4** (installed with PostgreSQL)
2. Expand "Servers" → "PostgreSQL 16"
3. Enter your postgres password
4. Right-click "Databases" → "Create" → "Database"
5. Name: `parking_booking_db`
6. Click "Save"
7. Right-click on `parking_booking_db` → "Query Tool"
8. Open file: `C:\Users\acer\.gemini\antigravity\scratch\parking-booking-app\database\setup.sql`
9. Click "Execute" (F5)
10. (Optional) Open and execute `sample-data.sql`

### Method 2: Using Command Line

```powershell
# Navigate to PostgreSQL bin directory
cd "C:\Program Files\PostgreSQL\16\bin"

# Connect to PostgreSQL
.\psql -U postgres

# You'll be prompted for password (enter the one you set during installation)

# Once connected, run:
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/setup.sql'

# (Optional) Load sample data:
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/sample-data.sql'

# Verify:
\c parking_booking_db
\dt
SELECT * FROM roles;

# Exit:
\q
```

### Method 3: Let Spring Boot Create Tables (Simplest)

If you don't want to run SQL scripts:

1. Just create an empty database:
   ```sql
   CREATE DATABASE parking_booking_db;
   ```

2. Update `backend/src/main/resources/application.properties` with your password:
   ```properties
   spring.datasource.password=your_password_here
   ```

3. Start the Spring Boot application:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

4. Hibernate will automatically create all tables!

---

## Update Application Configuration

After installing PostgreSQL, update the password in:

**File**: `backend/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_booking_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE  # Change this!
```

---

## Troubleshooting

### Issue: "psql is not recognized"

**Solution**: 
1. PostgreSQL is not installed, OR
2. PostgreSQL bin directory is not in PATH
3. Restart your terminal after adding to PATH

### Issue: "Connection refused"

**Solution**: 
1. Check if PostgreSQL service is running:
   - Press `Win + R`
   - Type `services.msc`
   - Look for "postgresql-x64-16" service
   - If stopped, right-click and "Start"

### Issue: "password authentication failed"

**Solution**: 
1. Use the password you set during installation
2. Update `application.properties` with correct password

---

## Quick Start (After Installation)

1. ✅ Install PostgreSQL
2. ✅ Add to PATH and restart terminal
3. ✅ Create database using pgAdmin or psql
4. ✅ Update password in `application.properties`
5. ▶️ Start backend: `cd backend && mvn spring-boot:run`
6. ▶️ Start frontend: `cd frontend && npm install && npm run dev`
7. 🌐 Open: `http://localhost:5173`

---

## Need Help?

- PostgreSQL Documentation: https://www.postgresql.org/docs/
- pgAdmin Documentation: https://www.pgadmin.org/docs/
- Video Tutorial: Search "Install PostgreSQL on Windows" on YouTube

Good luck! 🚀
