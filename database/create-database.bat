@echo off
REM Simple batch script to create the database using psql
REM This script will prompt for your PostgreSQL password

echo ========================================
echo Creating Parking Booking Database
echo ========================================
echo.

REM Add PostgreSQL to PATH for this session
set PATH=%PATH%;C:\Program Files\PostgreSQL\18\bin

echo Step 1: Creating database...
echo You will be prompted for your PostgreSQL password
echo.

REM Create database
psql -U postgres -c "CREATE DATABASE parking_booking_db;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Database created successfully!
    echo.
    echo Step 2: Running setup script...
    echo.
    
    REM Run setup script
    psql -U postgres -d parking_booking_db -f "%~dp0setup.sql"
    
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ✓ Database schema created successfully!
        echo.
        
        REM Ask if user wants sample data
        set /p LOAD_SAMPLE="Do you want to load sample data? (Y/N): "
        if /i "%LOAD_SAMPLE%"=="Y" (
            echo.
            echo Loading sample data...
            psql -U postgres -d parking_booking_db -f "%~dp0sample-data.sql"
            echo.
            echo ✓ Sample data loaded!
        )
        
        echo.
        echo ========================================
        echo Database setup complete!
        echo ========================================
        echo.
        echo Next steps:
        echo 1. Update password in backend\src\main\resources\application.properties
        echo 2. Run: cd backend ^&^& mvn spring-boot:run
        echo 3. Run: cd frontend ^&^& npm install ^&^& npm run dev
        echo.
    ) else (
        echo.
        echo ✗ Failed to create schema
        echo Please check the error messages above
    )
) else (
    echo.
    echo ✗ Failed to create database
    echo.
    echo Common issues:
    echo - Database might already exist (that's OK!)
    echo - Wrong password
    echo - PostgreSQL service not running
    echo.
    echo If database already exists, you can run setup.sql manually:
    echo   psql -U postgres -d parking_booking_db -f setup.sql
)

echo.
pause
