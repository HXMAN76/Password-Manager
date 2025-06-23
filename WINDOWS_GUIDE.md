# Using Password Manager on Windows with Docker

This guide will help you run the Password Manager application on Windows using Docker.

## Prerequisites

1. **Install Docker Desktop for Windows**
   - Download from: https://www.docker.com/products/docker-desktop
   - Install and start Docker Desktop

2. **Install an X Server for Windows**
   - Option 1: VcXsrv (Recommended) - https://sourceforge.net/projects/vcxsrv/
   - Option 2: Xming - https://sourceforge.net/projects/xming/
   - Option 3: MobaXterm - https://mobaxterm.mobatek.net/

## Setting Up X Server (Using VcXsrv as an example)

1. Install VcXsrv
2. Start XLaunch from your Start menu
3. Select "Multiple windows" and click Next
4. Select "Start no client" and click Next
5. On the X11 Extra settings page:
   - **IMPORTANT:** Check "Disable access control"
   - Click Next
6. Click Finish to start the X server
7. You may need to allow the application through Windows Firewall when prompted

## Running Password Manager

1. Double-click `setup_windows.bat` to set up the environment
   - This will build the Docker containers
   - Wait for the setup to complete (this might take a few minutes the first time)

2. Make sure your X server (VcXsrv, Xming, or MobaXterm) is running

3. Double-click `run_app_windows.bat` to start the Password Manager application
   - The application should appear after a few moments

## Troubleshooting

### If the application window doesn't appear:

1. **Check X Server**: Make sure your X server is running with "Disable access control" checked
2. **Check Docker**: Make sure Docker Desktop is running
3. **Firewall**: Ensure your firewall allows connections to your X server
   - Check Windows Firewall settings for VcXsrv or your X server application
4. **Check Docker logs**:
   - Open Command Prompt and run: `docker logs password-manager-app`
   - Look for any errors related to display or X11

### If you get database connection errors:

1. Make sure the MySQL container is running:
   - Open Command Prompt and run: `docker ps`
   - You should see "password-manager-mysql" in the list
2. Try restarting the setup process:
   - Run `docker compose -f docker-compose.windows.yml down -v`
   - Run `setup_windows.bat` again

## Stopping the Application

1. Close the Password Manager window
2. Press Ctrl+C in the command prompt window running the application
3. To fully stop and remove the containers, run:
   ```
   docker compose -f docker-compose.windows.yml down
   ```
   or
   ```
   docker-compose -f docker-compose.windows.yml down
   ```

## Clean Start

If you want to start fresh (including removing the database):
   ```
   docker compose -f docker-compose.windows.yml down -v
   ```
   or
   ```
   docker-compose -f docker-compose.windows.yml down -v
   ```
Then run `setup_windows.bat` again.
