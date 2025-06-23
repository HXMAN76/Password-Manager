package com.passwordmanager.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Configuration utility class to load and access configuration properties.
 * Supports configuration from both properties file and environment variables.
 */
public class ConfigManager {
    private static Properties properties = new Properties();
    private static boolean initialized = false;

    /**
     * Initialize the configuration manager.
     */
    public static void init() {
        if (!initialized) {
            try {
                // Try to load from classpath resource
                InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");
                
                // If not found in classpath, try to load from file system
                if (inputStream == null) {
                    try {
                        inputStream = new FileInputStream("config.properties");
                    } catch (Exception e) {
                        // If file not found, we'll use environment variables
                        System.out.println("No config.properties found, will use environment variables");
                    }
                }
                
                if (inputStream != null) {
                    properties.load(inputStream);
                    inputStream.close();
                }
                
                // Load environment variables which override file settings
                loadEnvironmentVariables();
                
                initialized = true;
            } catch (Exception e) {
                System.err.println("Failed to load configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Load configuration from environment variables
     */
    private static void loadEnvironmentVariables() {
        // Database configuration
        if (System.getenv("DB_URL") != null) {
            properties.setProperty("db.url", System.getenv("DB_URL"));
        } else if (System.getProperty("db.url") != null) {
            properties.setProperty("db.url", System.getProperty("db.url"));
        }
        
        if (System.getenv("DB_USERNAME") != null) {
            properties.setProperty("db.username", System.getenv("DB_USERNAME"));
        } else if (System.getProperty("db.username") != null) {
            properties.setProperty("db.username", System.getProperty("db.username"));
        }
        
        if (System.getenv("DB_PASSWORD") != null) {
            properties.setProperty("db.password", System.getenv("DB_PASSWORD"));
        } else if (System.getProperty("db.password") != null) {
            properties.setProperty("db.password", System.getProperty("db.password"));
        }
        
        // Other application settings
        if (System.getenv("PASSWORD_LENGTH") != null) {
            properties.setProperty("password.length", System.getenv("PASSWORD_LENGTH"));
        } else if (System.getProperty("password.length") != null) {
            properties.setProperty("password.length", System.getProperty("password.length"));
        }
    }

    /**
     * Get a property value as a string.
     * 
     * @param key The property key
     * @param defaultValue Default value if not found
     * @return The property value
     */
    public static String getString(String key, String defaultValue) {
        if (!initialized) init();
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get a property value as an integer.
     * 
     * @param key The property key
     * @param defaultValue Default value if not found
     * @return The property value as an integer
     */
    public static int getInt(String key, int defaultValue) {
        if (!initialized) init();
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * Get a property value as a boolean.
     * 
     * @param key The property key
     * @param defaultValue Default value if not found
     * @return The property value as a boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        if (!initialized) init();
        try {
            return Boolean.parseBoolean(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get a resource from the classpath.
     * 
     * @param path The resource path
     * @return The resource URL
     */
    public static URL getResource(String path) {
        return ConfigManager.class.getClassLoader().getResource(path);
    }
}
