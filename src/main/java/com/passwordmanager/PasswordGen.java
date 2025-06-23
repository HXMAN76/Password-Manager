package com.passwordmanager;

import java.security.SecureRandom;
import com.passwordmanager.util.ConfigManager;

/**
 * Utility class to generate secure random passwords.
 */
public class PasswordGen {
    private static final String CAPITAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SMALL_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_CHARS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()";
    private static final String ALL_CHARS = CAPITAL_CHARS + SMALL_CHARS + NUMBER_CHARS + SPECIAL_CHARS;
    
    private final SecureRandom random = new SecureRandom();
    
    /**
     * Generates a random password of a specified length.
     * 
     * @param length The length of the password to generate
     * @return The randomly generated password as a character array
     */
    public char[] generatePassword(int length) {
        if (length <= 0) {
            length = ConfigManager.getInt("password.length", 16);
        }
        
        char[] password = new char[length];
        
        // Ensure at least one character from each group
        password[0] = CAPITAL_CHARS.charAt(random.nextInt(CAPITAL_CHARS.length()));
        password[1] = SMALL_CHARS.charAt(random.nextInt(SMALL_CHARS.length()));
        password[2] = NUMBER_CHARS.charAt(random.nextInt(NUMBER_CHARS.length()));
        password[3] = SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
        
        // Fill the rest with random characters from all groups
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length()));
        }
        
        // Shuffle the password characters
        for (int i = 0; i < length; i++) {
            int randomPosition = random.nextInt(length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        
        return password;
    }
    
    /**
     * Convenience method that returns the password as a string.
     * 
     * @param length The length of the password
     * @return The password as a String
     */
    public String generatePasswordString(int length) {
        return new String(generatePassword(length));
    }
}
