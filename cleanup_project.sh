#!/bin/bash

echo "Cleaning up project structure..."

# Remove redundant class files
echo "Removing class files..."
find . -name "*.class" -type f -delete

# Clean up old project structure
if [ -d "Password_Manager" ]; then
    echo "Removing old project structure (Password_Manager directory)..."
    rm -rf Password_Manager/
fi

# Clean up Docker containers and volumes
echo "Cleaning up Docker containers and volumes..."
docker compose down -v 2>/dev/null
docker compose -f docker-compose.headless.yml down -v 2>/dev/null
docker compose -f docker-compose.windows.yml down -v 2>/dev/null

# Remove unwanted redundant files
echo "Removing redundant files..."
rm -f Dockerfile.simple docker-compose.simple.yml cleanup.sh setup_offline_mode.sh setup_github.sh run_app_docker.sh 2>/dev/null

# Clean up IDE-specific files
echo "Cleaning up IDE files..."
rm -rf .idea/ .classpath .project .settings/ *.iml 2>/dev/null

# Make sure all shell scripts are executable
echo "Making all shell scripts executable..."
chmod +x *.sh

echo "Project cleanup complete!"
echo ""
echo "Removed files:"
echo "- Dockerfile.simple (merged into Dockerfile)"
echo "- docker-compose.simple.yml (merged into docker-compose.yml)"
echo "- cleanup.sh (replaced by cleanup_project.sh)"
echo "- setup_offline_mode.sh (no longer needed)"
echo "- setup_github.sh (not relevant for Docker workflow)"
echo "- run_app_docker.sh (replaced by run_app.sh)"
echo "- Various IDE-specific files (.idea/, *.iml, etc.)"
echo ""
echo "Final project structure is now clean and organized."
echo "You can run the application with:"
echo "- ./run_app.sh (Linux/Mac)"
echo "- run_app_windows.bat (Windows)"
