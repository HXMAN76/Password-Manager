#!/bin/bash
# Start Password Manager application with GUI

# Allow connections to X server from local containers
xhost +local: > /dev/null 2>&1 || echo "Warning: Could not set xhost permissions"

# Start the application
echo "Starting Password Manager..."
docker compose up app

# Revoke X server permissions
xhost -local: > /dev/null 2>&1 || true
