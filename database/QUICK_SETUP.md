# Quick Setup Steps - PostgreSQL Already Installed

Since you've installed PostgreSQL, follow these quick steps to complete the setup:

## Step 1: Add PostgreSQL to PATH

You need to add PostgreSQL to your system PATH so you can use `psql` commands.

### Option A: Automatic (Run this in PowerShell as Administrator)

```powershell
# Find your PostgreSQL version
$pgVersion = (Get-ChildItem "C:\Program Files\PostgreSQL" -Directory | Select-Object -First 1).Name

# Add to PATH
$pgBinPath = "C:\Program Files\PostgreSQL\$pgVersion\bin"
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";$pgBinPath", [EnvironmentVariableTarget]::Machine)

Write-Host "PostgreSQL added to PATH: $pgBinPath"
Write-Host "Please RESTART your PowerShell/Terminal for changes to take effect!"
```

### Option B: Manual (Recommended if you're not comfortable with scripts)

1. Press `Win + X` and select "System"
2. Click "Advanced system settings" on the right
3. Click "Environment Variables" button
4. Under "System variables", find and select "Path"
5. Click "Edit"
6. Click "New"
7. Add: `C:\Program Files\PostgreSQL\17\bin` (adjust version number if different)
8. Click "OK" on all windows
9. **IMPORTANT**: Close and reopen your PowerShell/Terminal

## Step 2: Verify PostgreSQL is Working

After restarting your terminal:

```powershell
psql --version
```

You should see something like: `psql (PostgreSQL) 17.x`

## Step 3: Create the Database

### Option A: Using pgAdmin (GUI - Easiest)

1. Open **pgAdmin 4** (search in Start menu)
2. Expand "Servers" → "PostgreSQL 17" (or your version)
3. Enter your postgres password (the one you set during installation)
4. Right-click "Databases" → "Create" → "Database"
5. Database name: `parking_booking_db`
6. Owner: `postgres`
7. Click "Save"

### Option B: Using Command Line

```powershell
# Connect to PostgreSQL (you'll be prompted for password)
psql -U postgres

# Once connected, create the database
CREATE DATABASE parking_booking_db;

# Verify
\l

# Exit
\q
```

## Step 4: Run Setup Scripts

### Using pgAdmin (Recommended):

1. In pgAdmin, right-click on `parking_booking_db`
2. Select "Query Tool"
3. Click "Open File" icon
4. Navigate to: `C:\Users\acer\.gemini\antigravity\scratch\parking-booking-app\database\setup.sql`
5. Click "Execute" (F5 or ▶️ button)
6. You should see "Query returned successfully"

7. (Optional) Load sample data:
   - Open file: `sample-data.sql`
   - Click "Execute"

### Using Command Line:

```powershell
# Connect to PostgreSQL
psql -U postgres -d parking_booking_db

# Run setup script
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/setup.sql'

# (Optional) Load sample data
\i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/sample-data.sql'

# Verify tables were created
\dt

# Check roles
SELECT * FROM roles;

# Exit
\q
```

## Step 5: Update Application Configuration

Update the password in `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_booking_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE  # Change this to your postgres password!
```

## Step 6: You're Ready!

Now you can start the application:

```bash
# Start backend
cd backend
mvn spring-boot:run

# In another terminal, start frontend
cd frontend
npm install
npm run dev
```

## Troubleshooting

### "psql is not recognized" after adding to PATH
- **Solution**: You MUST restart your terminal/PowerShell after adding to PATH

### Can't remember PostgreSQL password
- **Solution**: You can reset it by editing `pg_hba.conf` or reinstalling PostgreSQL

### Port 5432 already in use
- **Solution**: Another PostgreSQL instance is running, or you need to stop other services using that port

### Connection refused
- **Solution**: 
  1. Press `Win + R`, type `services.msc`
  2. Find "postgresql-x64-17" (or your version)
  3. Right-click → "Start"

## Default Test Accounts (After Running sample-data.sql)

- **Admin**: admin@parkingbook.com / admin123
- **Customer**: john@example.com / password123
- **Owner**: owner1@example.com / password123

---

**Next Step**: After completing these steps, let me know and I'll help you start the application! 🚀
