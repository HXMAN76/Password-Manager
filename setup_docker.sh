#!/bin/bash

echo "Password Manager Docker Setup"
echo "----------------------------"

# Check if Docker is installed and running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "Cleaning up any existing containers..."
docker compose down -v 2>/dev/null || docker-compose down -v 2>/dev/null

# Try running with both docker compose and docker-compose
echo "Starting MySQL database..."
docker compose up -d mysql 2>/dev/null || docker-compose up -d mysql

echo "Waiting for MySQL to initialize (this may take a minute)..."
sleep 15

echo "Building the application..."
docker compose build app 2>/dev/null || docker-compose build app

echo "Setup complete!"
echo "To run the Password Manager, execute: ./run_app.sh"
