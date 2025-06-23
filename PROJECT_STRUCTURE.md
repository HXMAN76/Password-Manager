# Project Structure

This document outlines the simplified project structure for the Password Manager application.

## Directory Structure

```
/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── passwordmanager/
│   │   │           ├── App.java             # Main application class
│   │   │           ├── Manager.java         # Database operations manager
│   │   │           ├── PasswordGen.java     # Password generator
│   │   │           └── util/
│   │   │               └── ConfigManager.java # Configuration utility
│   │   └── resources/
│   │       ├── config.properties           # Application configuration
│   │       ├── database.sql                # SQL script for database setup
│   │       ├── images/
│   │       │   └── Amritalogo.png          # Logo image
│   │       └── lib/
│   │           └── mysql-connector-j-8.4.0.jar # MySQL connector
├── docker-compose.yml                   # Main Docker Compose file (Linux/Mac)
├── docker-compose.windows.yml           # Docker Compose for Windows
├── docker-compose.headless.yml          # Docker Compose for headless mode
├── Dockerfile                           # Docker image definition
├── pom.xml                              # Maven project file
├── PROJECT_STRUCTURE.md                 # This file
├── README.md                            # Project documentation
├── run_app.sh                           # Script to run the app with Docker on Linux/Mac
├── run_app_windows.bat                  # Script to run with Docker on Windows
├── setup_database.sh                    # Script for database setup (local or Docker)
├── setup_docker.sh                      # Script to set up Docker environment on Linux/Mac
├── setup_windows.bat                    # Script to set up Docker on Windows
├── run_headless_mode.sh                 # Script to run in headless mode
├── install_docker_compose.sh            # Helper script for installing Docker Compose
├── cleanup_project.sh                   # Script to clean up project structure
└── WINDOWS_GUIDE.md                     # Guide for Windows users
```

## Key Components

1. **App.java**: The main application class that handles the GUI and user interactions
2. **Manager.java**: Handles database operations for storing and retrieving passwords
3. **PasswordGen.java**: Generates secure random passwords
4. **ConfigManager.java**: Handles configuration file loading and access

## Docker Components

1. **Dockerfile**: Defines the containerized environment for the application
2. **docker-compose.yml**: Orchestrates the application and database containers for Linux/Mac
3. **docker-compose.windows.yml**: Specialized Docker Compose for Windows X11 support
4. **docker-compose.headless.yml**: Docker Compose for headless mode (MySQL + phpMyAdmin)

## Scripts

### Docker Setup & Execution
- **run_app.sh**: Runs the application in Docker on Linux/Mac
- **run_app_windows.bat**: Runs the application in Docker on Windows
- **setup_docker.sh**: Sets up Docker environment on Linux/Mac
- **setup_windows.bat**: Sets up Docker environment on Windows
- **run_headless_mode.sh**: Runs MySQL and phpMyAdmin without the GUI application

### Maintenance
- **cleanup_project.sh**: Removes redundant files and keeps the project structure clean
- **install_docker_compose.sh**: Helper script for installing Docker Compose
