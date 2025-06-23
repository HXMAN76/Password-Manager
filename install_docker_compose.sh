#!/bin/bash

echo "Docker Compose Installation Helper"
echo "---------------------------------"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker first."
    echo "Visit https://docs.docker.com/get-docker/ for installation instructions."
    exit 1
fi

echo "Docker is installed. Checking for Docker Compose..."

# Check if the docker compose plugin is already available
if docker compose version &> /dev/null; then
    echo "Docker Compose plugin is already installed."
    docker compose version
    exit 0
fi

# Check if standalone docker-compose is installed
if command -v docker-compose &> /dev/null; then
    echo "Standalone docker-compose is installed."
    docker-compose --version
    exit 0
fi

# Try to install Docker Compose plugin
echo "Installing Docker Compose plugin..."
echo "This might require sudo permission."

# Try different installation methods based on distribution
if command -v apt &> /dev/null; then
    echo "Detected Debian/Ubuntu-based system..."
    echo "Running: sudo apt update && sudo apt install -y docker-compose-plugin"
    sudo apt update && sudo apt install -y docker-compose-plugin
elif command -v dnf &> /dev/null; then
    echo "Detected Fedora/RHEL-based system..."
    echo "Running: sudo dnf install -y docker-compose-plugin"
    sudo dnf install -y docker-compose-plugin
elif command -v pacman &> /dev/null; then
    echo "Detected Arch-based system..."
    echo "Running: sudo pacman -S --noconfirm docker-compose"
    sudo pacman -S --noconfirm docker-compose
else
    echo "Could not determine your distribution's package manager."
    echo "Please install Docker Compose manually:"
    echo ""
    echo "For the plugin version:"
    echo "  Follow instructions at: https://docs.docker.com/compose/install/linux/"
    echo ""
    echo "For the standalone version:"
    echo "  Follow instructions at: https://docs.docker.com/compose/install/standalone/"
    exit 1
fi

# Verify the installation
echo "Verifying Docker Compose installation..."
if docker compose version &> /dev/null; then
    echo "Docker Compose plugin installed successfully!"
    docker compose version
elif command -v docker-compose &> /dev/null; then
    echo "Standalone docker-compose installed successfully!"
    docker-compose --version
else
    echo "Docker Compose installation could not be verified."
    echo "You may need to restart your shell or system."
    echo "If issues persist, please install Docker Compose manually:"
    echo "https://docs.docker.com/compose/install/"
    exit 1
fi

echo ""
echo "Docker Compose is now installed! You can run the application with:"
echo "./run_app_docker.sh"
