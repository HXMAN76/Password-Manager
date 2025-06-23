#!/bin/bash

echo "Password Manager Setup Options"
echo "----------------------------"
echo "1. Use Docker (recommended)"
echo "2. Use local MySQL"
echo ""

read -p "Select option (1-2): " option

case $option in
    1)
        echo "Using Docker setup..."
        ./setup_docker.sh
        ;;
    2)
        echo "Using local MySQL setup..."
        
        # Check if MySQL client is installed
        if ! command -v mysql &> /dev/null; then
            echo "MySQL client is not installed. Please install it first."
            exit 1
        fi

        # Set variables
        DB_NAME="Password_Manager"
        DB_USER="root"
        DB_PASS=""

        # Read username and password from user input
        read -p "Enter MySQL username (default: root): " input_user
        DB_USER=${input_user:-$DB_USER}

        read -s -p "Enter MySQL password (press Enter if none): " input_pass
        echo ""
        DB_PASS=${input_pass:-$DB_PASS}

        echo "Creating database and tables..."

        # If password is empty, don't use -p parameter
        if [ -z "$DB_PASS" ]; then
            mysql -u "$DB_USER" << EOF
CREATE DATABASE IF NOT EXISTS $DB_NAME;
USE $DB_NAME;

CREATE TABLE IF NOT EXISTS password (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    app_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_app (username, app_name)
);
EOF
        else
            mysql -u "$DB_USER" -p"$DB_PASS" << EOF
CREATE DATABASE IF NOT EXISTS $DB_NAME;
USE $DB_NAME;

CREATE TABLE IF NOT EXISTS password (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    app_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_app (username, app_name)
);
EOF
        fi

        # Check if the database was created successfully
        if [ $? -eq 0 ]; then
            echo "Database setup completed successfully!"
            
            # Update the config.properties file
            CONFIG_FILE="src/main/resources/config.properties"
            if [ -f "$CONFIG_FILE" ]; then
                sed -i "s/db.username=.*/db.username=$DB_USER/" "$CONFIG_FILE"
                sed -i "s/db.password=.*/db.password=$DB_PASS/" "$CONFIG_FILE"
                echo "Configuration file updated with your database credentials."
            else
                echo "Warning: Configuration file not found at $CONFIG_FILE"
            fi
        else
            echo "Database setup failed. Please check your MySQL installation and credentials."
        fi
        ;;
    *)
        echo "Invalid option. Please run the script again and select 1 or 2."
        exit 1
        ;;
esac
