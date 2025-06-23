package com.passwordmanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.passwordmanager.util.ConfigManager;

/**
 * Manager class for handling database operations related to passwords.
 */
public class Manager {
    private String password;
    private final PasswordGen passwordGenerator = new PasswordGen();

    /**
     * Gets a database connection based on configuration.
     * 
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    private Connection getConnection() throws SQLException {
        String url = ConfigManager.getString("db.url", "jdbc:mysql://localhost:3306/Password_Manager");
        String dbUsername = ConfigManager.getString("db.username", "root");
        String dbPassword = ConfigManager.getString("db.password", "");
        
        // For MySQL 8.0 and later, we need to specify these options
        String fullUrl = url + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        
        return DriverManager.getConnection(fullUrl, dbUsername, dbPassword);
    }

    /**
     * Checks if a username and app name combination already exists.
     * 
     * @param username The username
     * @param appName The application name
     * @return True if the combination exists, false otherwise
     */
    public boolean ifExists(String username, String appName) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM password WHERE username = ? AND app_name = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, appName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generates a password and stores it in the database.
     * 
     * @param username The username
     * @param appName The application name
     * @return True if successful, false otherwise
     */
    public boolean generatePassword(String username, String appName) {
        int passwordLength = ConfigManager.getInt("password.length", 16);
        password = passwordGenerator.generatePasswordString(passwordLength);
        return enterPassword(username, password, appName);
    }

    /**
     * Retrieves passwords for a specific application.
     * 
     * @param appName The application name
     * @return List of username/password entries
     */
    public List<String> showData(String appName) {
        List<String> data = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT username, password FROM password WHERE app_name = ?")) {
            stmt.setString(1, appName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String pwd = rs.getString("password");
                    data.add("- User Name: " + username + "\n- Password: " + pwd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Enters a user-specified password into the database.
     * 
     * @param username The username
     * @param password The password
     * @param appName The application name
     * @return True if successful, false otherwise
     */
    public boolean enterPassword(String username, String password, String appName) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO password (username, password, app_name) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, appName);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes an existing password.
     * 
     * @param username The username
     * @param password The new password (or empty to generate one)
     * @param appName The application name
     * @return True if successful, false otherwise
     */
    public boolean changeData(String username, String password, String appName) {
        if (password == null || password.isEmpty()) {
            int passwordLength = ConfigManager.getInt("password.length", 16);
            password = passwordGenerator.generatePasswordString(passwordLength);
        }
        
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement("UPDATE password SET password = ? WHERE app_name = ? AND username = ?")) {
            stmt.setString(1, password);
            stmt.setString(2, appName);
            stmt.setString(3, username);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a password entry.
     * 
     * @param appName The application name
     * @param username The username
     * @return True if successful, false otherwise
     */
    public boolean deleteData(String appName, String username) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM password WHERE app_name = ? AND username = ?")) {
            stmt.setString(1, appName);
            stmt.setString(2, username);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets the most recently generated or accessed password.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }
}
