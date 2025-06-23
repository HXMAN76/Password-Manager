# Password Manager

A secure password management application built in Java with a user-friendly GUI interface. This application allows you to generate, store, retrieve, and manage passwords for various applications.

## Features

- **Secure Password Generation**: Create strong random passwords
- **Password Storage**: Store passwords securely in a MySQL database
- **Password Retrieval**: Look up passwords by app name
- **Password Management**: Update or delete existing passwords
- **User-Friendly Interface**: Simple and intuitive GUI
- **Containerized**: Run anywhere with Docker without installation hassle

## Running with Docker (Recommended)

### Prerequisites

- Docker and Docker Compose
- For GUI applications in Docker:
  - **On Windows**: X Server software like VcXsrv (recommended), Xming, or MobaXterm
  - **On Linux/Mac**: X11 enabled (standard on most distros)

### Quick Docker Start

#### For Windows Users

1. Install Docker Desktop for Windows
2. Install an X server (VcXsrv recommended) and start it with "Disable access control" checked
3. Run the setup script:
   ```
   setup_windows.bat
   ```
4. Run the application:
   ```
   run_app_windows.bat
   ```

See [WINDOWS_GUIDE.md](WINDOWS_GUIDE.md) for detailed Windows instructions.

#### For Linux/Mac Users

1. Run the setup script:
   ```bash
   ./setup_database.sh
   ```
2. Run the application:
   ```bash
   ./run_app.sh
   ```

### Troubleshooting Docker Setup

If you encounter issues with the application:

#### Database Connection Issues

If you have problems connecting to the database:

1. Check if MySQL container is running:
   ```bash
   docker ps | grep mysql
   ```

2. Check MySQL logs:
   ```bash
   docker logs password-manager-mysql
   ```

3. Clean up and restart:
   ```bash
   docker compose down -v
   ./setup_database.sh  # or setup_windows.bat on Windows
   ```

#### Display/GUI Issues

If the GUI doesn't appear:

1. Make sure X11 forwarding is working:
   - On Linux: Run `xhost +local:` before starting the app
   - On Windows: Ensure your X server is running with "Disable access control" checked

2. Check application logs:
   ```bash
   docker logs password-manager-app
   ```

#### Headless Mode

You can run the database in headless mode without the GUI application:
```bash
./run_headless_mode.sh
```

This will start:
- MySQL database server on port 3306
- phpMyAdmin web interface at http://localhost:8080 (username: root, password: password)

## Manual Installation

### Prerequisites

- Java 11 or higher
- MySQL or MariaDB
- Maven 3.6 or higher (for building)

### Quick Start

The project includes two convenient scripts for setup and running:

1. **Setup Database**: Run the database setup script
   ```bash
   ./setup_database.sh
   ```
   This script will create the database and tables, and update the configuration file with your database credentials.

2. **Run Application**: Build and run the application
   ```bash
   ./run_app.sh
   ```
   This script will build the project with Maven and run the application.

## Manual Setup

If you prefer manual setup, follow these steps:

### Database Setup

1. Create a MySQL/MariaDB database named `Password_Manager`
2. Run the SQL script in `src/main/resources/database.sql` to set up the necessary tables:

```bash
mysql -u root -p < src/main/resources/database.sql
```

### Configuration

The application uses `config.properties` in the `src/main/resources` directory. Edit this file to match your database settings:

```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/Password_Manager
db.username=root
db.password=your_password_here
db.driver=com.mysql.cj.jdbc.Driver

# Application Settings
password.length=16
app.name=Password Manager
```

### Building the Application

To build the application manually, run:

```bash
mvn clean package
```

This will create an executable JAR file with all dependencies in the `target` directory.

### Running the Application

To run the application manually:

```bash
java -jar target/password-manager-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Usage

1. **Generate Password**: Enter the username and app name, then click "Generate Password"
2. **Enter Password**: Fill in the username, app name, and password, then click "Enter Password"
3. **Find Password**: Enter the app name and click "Find Password" to retrieve all passwords for that app
4. **Delete Password**: Enter the username and app name, then click "Delete Password" to remove the entry

## Security Notes

- Passwords are stored in plaintext in the database - in a production environment, consider using encryption
- Database credentials are stored in a configuration file - ensure file permissions are set appropriately
- This application is designed for educational purposes and personal use

## License

This project is open source and available under the [MIT License](LICENSE).

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Future Improvements

- Encryption of passwords in the database
- User authentication system
- Export/import functionality
- Dark mode
- Automatic clipboard copying
