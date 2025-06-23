#!/bin/bash

echo "Starting Password Manager in Headless Mode"
echo "----------------------------------------"
echo "This will start only the MySQL database and phpMyAdmin"
echo "The Java GUI application will not run in this mode"
echo "You can access phpMyAdmin at http://localhost:8080"
echo ""

# Check if Docker is installed and running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Try running with both docker compose and docker-compose
echo "Starting MySQL database and phpMyAdmin..."

if docker compose version > /dev/null 2>&1; then
    docker compose -f docker-compose.headless.yml up -d
elif command -v docker-compose > /dev/null 2>&1; then
    docker-compose -f docker-compose.headless.yml up -d
else
    echo "Could not find docker compose or docker-compose command."
    echo "Please install Docker Compose:"
    echo "Run ./install_docker_compose.sh"
    exit 1
fi

echo ""
echo "Services started in headless mode:"
echo "- MySQL database is running on port 3306"
echo "- phpMyAdmin interface is available at: http://localhost:8080"
echo ""
echo "Login credentials for phpMyAdmin:"
echo "- Username: root"
echo "- Password: password"
echo ""
echo "To stop these services, run:"
echo "docker compose -f docker-compose.headless.yml down"
echo "or"
echo "docker-compose -f docker-compose.headless.yml down"
