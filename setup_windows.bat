@echo off
echo Docker Password Manager Setup
echo ----------------------------

echo Checking if Docker is installed...
where docker >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker is not installed. Please install Docker Desktop for Windows first.
    echo Download from: https://www.docker.com/products/docker-desktop
    pause
    exit /b
)

echo Checking if Docker is running...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker is not running. Please start Docker Desktop and try again.
    pause
    exit /b
)

echo Cleaning up any existing containers...
docker compose -f docker-compose.windows.yml down -v 2>NUL || (
    docker-compose -f docker-compose.windows.yml down -v 2>NUL
)

echo Building and starting the MySQL container...
docker compose -f docker-compose.windows.yml up -d --build mysql 2>NUL || (
    echo Using legacy docker-compose command...
    docker-compose -f docker-compose.windows.yml up -d --build mysql
)

echo Waiting for MySQL to initialize (this may take a minute)...
timeout /t 30 /nobreak

echo Building the application container...
docker compose -f docker-compose.windows.yml build app 2>NUL || (
    echo Using legacy docker-compose command...
    docker-compose -f docker-compose.windows.yml build app
)

echo Setup complete!
echo To start the Password Manager:
echo 1. Make sure your X server (VcXsrv, Xming, or MobaXterm) is running
echo 2. Run the run_app_windows.bat file
pause
