-- Create database if not exists
CREATE DATABASE IF NOT EXISTS Password_Manager;

USE Password_Manager;

-- Create password table if not exists
CREATE TABLE IF NOT EXISTS password (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    app_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_app (username, app_name)
);

-- Sample insert statement (commented out for safety)
-- INSERT INTO password (username, password, app_name) VALUES ('test', 'password123', 'Sample App');
