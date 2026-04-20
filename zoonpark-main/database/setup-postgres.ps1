# PostgreSQL Setup Script
# Run this script in PowerShell AS ADMINISTRATOR

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "PostgreSQL Database Setup for Parking Booking System" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Add PostgreSQL to PATH
Write-Host "Step 1: Adding PostgreSQL to PATH..." -ForegroundColor Yellow
$pgBinPath = "C:\Program Files\PostgreSQL\18\bin"

if (Test-Path $pgBinPath) {
    $currentPath = [Environment]::GetEnvironmentVariable("Path", [EnvironmentVariableTarget]::Machine)
    
    if ($currentPath -notlike "*$pgBinPath*") {
        try {
            [Environment]::SetEnvironmentVariable("Path", $currentPath + ";$pgBinPath", [EnvironmentVariableTarget]::Machine)
            Write-Host "✅ PostgreSQL added to PATH successfully!" -ForegroundColor Green
            Write-Host "   Path: $pgBinPath" -ForegroundColor Gray
            Write-Host ""
            Write-Host "⚠️  IMPORTANT: You must RESTART your PowerShell/Terminal for PATH changes to take effect!" -ForegroundColor Red
            Write-Host ""
        } catch {
            Write-Host "❌ Failed to add to PATH. Please run this script as Administrator." -ForegroundColor Red
            Write-Host "   Or add manually: $pgBinPath" -ForegroundColor Gray
            exit 1
        }
    } else {
        Write-Host "✅ PostgreSQL is already in PATH" -ForegroundColor Green
        Write-Host ""
    }
} else {
    Write-Host "❌ PostgreSQL bin directory not found at: $pgBinPath" -ForegroundColor Red
    Write-Host "   Please check your PostgreSQL installation." -ForegroundColor Gray
    exit 1
}

# Step 2: Instructions for database creation
Write-Host "Step 2: Next Steps to Create Database" -ForegroundColor Yellow
Write-Host ""
Write-Host "After restarting your terminal, run ONE of these options:" -ForegroundColor Cyan
Write-Host ""

Write-Host "Option A - Using pgAdmin (GUI - Easiest):" -ForegroundColor Green
Write-Host "  1. Open pgAdmin 4 from Start menu" -ForegroundColor Gray
Write-Host "  2. Expand Servers → PostgreSQL 18" -ForegroundColor Gray
Write-Host "  3. Enter your postgres password" -ForegroundColor Gray
Write-Host "  4. Right-click Databases → Create → Database" -ForegroundColor Gray
Write-Host "  5. Name: parking_booking_db" -ForegroundColor Gray
Write-Host "  6. Click Save" -ForegroundColor Gray
Write-Host "  7. Right-click parking_booking_db → Query Tool" -ForegroundColor Gray
Write-Host "  8. Open and execute: database\setup.sql" -ForegroundColor Gray
Write-Host "  9. (Optional) Open and execute: database\sample-data.sql" -ForegroundColor Gray
Write-Host ""

Write-Host "Option B - Using Command Line:" -ForegroundColor Green
Write-Host "  1. Restart your PowerShell/Terminal" -ForegroundColor Gray
Write-Host "  2. Run: psql -U postgres" -ForegroundColor Gray
Write-Host "  3. Enter your postgres password" -ForegroundColor Gray
Write-Host "  4. Run: CREATE DATABASE parking_booking_db;" -ForegroundColor Gray
Write-Host "  5. Run: \c parking_booking_db" -ForegroundColor Gray
Write-Host "  6. Run: \i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/setup.sql'" -ForegroundColor Gray
Write-Host "  7. (Optional) Run: \i 'C:/Users/acer/.gemini/antigravity/scratch/parking-booking-app/database/sample-data.sql'" -ForegroundColor Gray
Write-Host "  8. Run: \q to exit" -ForegroundColor Gray
Write-Host ""

Write-Host "Option C - Let Spring Boot Create Tables (Simplest):" -ForegroundColor Green
Write-Host "  1. Using pgAdmin or psql, just create empty database: CREATE DATABASE parking_booking_db;" -ForegroundColor Gray
Write-Host "  2. Update password in: backend\src\main\resources\application.properties" -ForegroundColor Gray
Write-Host "  3. Run: cd backend && mvn spring-boot:run" -ForegroundColor Gray
Write-Host "  4. Hibernate will create all tables automatically!" -ForegroundColor Gray
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Setup script completed!" -ForegroundColor Cyan
Write-Host "Remember to RESTART your terminal before using psql commands!" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
