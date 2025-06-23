package com.passwordmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;
import com.passwordmanager.util.ConfigManager;

/**
 * Main application class for the Password Manager.
 */
public class App extends JFrame implements ActionListener {
    private JTextField usernameField, appNameField, passwordField;
    private JTextArea outputArea;
    private JButton generateButton, enterButton, findButton, deleteButton;
    private Manager manager;
    private JPanel headerPanel;

    /**
     * Constructor for the App class.
     */
    public App() {
        super("Password Manager");
        // Initialize configuration
        ConfigManager.init();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create header panel
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 51, 51));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create logo
        JLabel logoLabel;
        try {
            // Try from resources
            URL logoUrl = ConfigManager.getResource("images/Amritalogo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                logoLabel = new JLabel(logoIcon);
            } else {
                // Try from filesystem for Docker setup
                ImageIcon logoIcon = new ImageIcon("images/Amritalogo.png");
                logoLabel = new JLabel(logoIcon);
            }
        } catch (Exception e) {
            // If image can't be loaded, use text instead
            logoLabel = new JLabel("Password Manager");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            System.err.println("Could not load logo image: " + e.getMessage());
        }
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Create heading text
        JLabel headingLabel = new JLabel("Password Manager");
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headingLabel, BorderLayout.CENTER);

        // Create input panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("App Name:"));
        appNameField = new JTextField();
        inputPanel.add(appNameField);
        inputPanel.add(new JLabel("Password:"));
        passwordField = new JTextField();
        inputPanel.add(passwordField);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 0, 0));
        generateButton = createButton("Generate Password");
        enterButton = createButton("Enter Password");
        findButton = createButton("Find Password");
        deleteButton = createButton("Delete Password");
        
        buttonPanel.add(generateButton);
        buttonPanel.add(enterButton);
        buttonPanel.add(findButton);
        buttonPanel.add(deleteButton);

        // Create output area
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST);
        
        // Set fixed window size
        setSize(800, 600);
        setResizable(false);

        pack();
        setLocationRelativeTo(null);
        
        // Initialize manager
        manager = new Manager();
    }
    
    /**
     * Helper method to create consistently styled buttons.
     * 
     * @param text The button text
     * @return The created button
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setBackground(new Color(0, 0, 255));
        button.setForeground(new Color(255, 255, 255));
        return button;
    }

    /**
     * Action handler for button events.
     * 
     * @param e The action event
     */
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String appName = appNameField.getText();
        String password = passwordField.getText();
        
        // Validate inputs
        if (username.isEmpty() || appName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and App Name are required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (e.getSource() == generateButton) {
            handleGeneratePassword(username, appName);
        } else if (e.getSource() == enterButton) {
            handleEnterPassword(username, appName, password);
        } else if (e.getSource() == findButton) {
            handleFindPassword(appName);
        } else if (e.getSource() == deleteButton) {
            handleDeletePassword(username, appName);
        }
    }
    
    /**
     * Handles the generate password action.
     * 
     * @param username The username
     * @param appName The application name
     */
    private void handleGeneratePassword(String username, String appName) {
        if (manager.ifExists(username, appName)) {
            int result = JOptionPane.showConfirmDialog(this, 
                "The username for the app already exists. Do you want to change the password?", 
                "Confirm", JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                boolean success = manager.changeData(username, "", appName);
                if (success) {
                    outputArea.setText("Password changed successfully.\nNew password: " + manager.getPassword());
                } else {
                    outputArea.setText("Failed to change password.");
                }
            }
        } else {
            boolean success = manager.generatePassword(username, appName);
            if (success) {
                outputArea.setText("The generated password for " + appName + " is: " + manager.getPassword());
            } else {
                outputArea.setText("Failed to generate password.");
            }
        }
    }
    
    /**
     * Handles the enter password action.
     * 
     * @param username The username
     * @param appName The application name
     * @param password The password
     */
    private void handleEnterPassword(String username, String appName, String password) {
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password or use the Generate Password button.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (manager.ifExists(username, appName)) {
            int result = JOptionPane.showConfirmDialog(this, 
                "The username for the app already exists. Do you want to change the password?", 
                "Confirm", JOptionPane.YES_NO_OPTION);
                
            if (result == JOptionPane.YES_OPTION) {
                boolean success = manager.changeData(username, password, appName);
                if (success) {
                    outputArea.setText("Password changed successfully");
                } else {
                    outputArea.setText("Failed to change password");
                }
            }
        } else {
            boolean success = manager.enterPassword(username, password, appName);
            if (success) {
                outputArea.setText("Password entered successfully");
            } else {
                outputArea.setText("Failed to enter password");
            }
        }
    }
    
    /**
     * Handles the find password action.
     * 
     * @param appName The application name
     */
    private void handleFindPassword(String appName) {
        List<String> data = manager.showData(appName);
        
        if (data.isEmpty()) {
            outputArea.setText("No passwords found for " + appName);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Passwords for ").append(appName).append(":\n\n");
            for (String entry : data) {
                sb.append(entry).append("\n\n");
            }
            outputArea.setText(sb.toString());
        }
    }
    
    /**
     * Handles the delete password action.
     * 
     * @param username The username
     * @param appName The application name
     */
    private void handleDeletePassword(String username, String appName) {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this password?", 
            "Confirm", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            boolean success = manager.deleteData(appName, username);
            if (success) {
                outputArea.setText("Password deleted successfully");
            } else {
                outputArea.setText("Failed to delete password");
            }
        }
    }

    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set look and feel to match system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App app = new App();
                app.setVisible(true);
            }
        });
    }
}
