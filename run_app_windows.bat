@echo off
echo Starting Password Manager
echo -----------------------

echo Checking if Docker is installed and running...
docker info >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker is not running. Please start Docker Desktop and try again.
    pause
    exit /b
)

echo Starting Password Manager application...

REM Checking for X server software
echo Checking for X server...
echo Please make sure you have X server software installed and running:
echo - VcXsrv (Recommended)
echo - Xming
echo - MobaXterm

REM Set the DISPLAY variable for X11 forwarding (Windows-specific)
set DISPLAY=host.docker.internal:0.0

REM Check if the MySQL container is running first
echo Checking if MySQL container is running...
docker ps | find "password-manager-mysql" >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo MySQL container is not running. Starting containers...
    docker compose -f docker-compose.windows.yml up -d mysql 2>NUL || (
        docker-compose -f docker-compose.windows.yml up -d mysql
    )
    echo Waiting for MySQL to start...
    timeout /t 10 /nobreak
)

REM Run the application with X11 forwarding
echo Starting application container...
docker compose -f docker-compose.windows.yml up app 2>NUL || (
    echo Using legacy docker-compose command...
    docker-compose -f docker-compose.windows.yml up app
)

pause
