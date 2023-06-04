package com.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String OTHER_SPECIAL_CHARS = "!@5%-_=+:./2";
    private static final String ALL_CHARACTERS = CHAR_LOWER + CHAR_UPPER + DIGIT + OTHER_SPECIAL_CHARS;

    private static SecureRandom random = new SecureRandom();

    public static String generatePassword() {
        List<Character> charList = new ArrayList<>();
        // Add at least one character from each category
        charList.add(getRandomChar(CHAR_LOWER));
        charList.add(getRandomChar(CHAR_UPPER));
        charList.add(getRandomChar(DIGIT));
        charList.add(getRandomChar(OTHER_SPECIAL_CHARS));

        // Add random characters until length is between 8 and 26
        while (charList.size() < 8 || charList.size() > 26) {
            charList.add(getRandomChar(ALL_CHARACTERS));
        }

        // Shuffle the list to make order random
        Collections.shuffle(charList, random);

        // Convert the list to a string
        StringBuilder sb = new StringBuilder();
        for (Character c : charList) {
            sb.append(c);
        }
        String password = sb.toString();

        // Check if password violates username and Administrator rules
        String username = "example_username"; // Replace with actual username
        if (password.contains(username) || isReversedUsernameSubString(password, username)
                || password.contains("Administrator") || isSubstringWithMoreThanTwoConsecutiveChars(password, "Administrator")) {
            // Generate a new password if there is a violation
            return generatePassword();
        } else {
            return password;
        }
    }

    private static char getRandomChar(String charSet) {
        int randomIndex = random.nextInt(charSet.length());
        return charSet.charAt(randomIndex);
    }

    private static boolean isReversedUsernameSubString(String password, String username) {
        StringBuilder reversedUsername = new StringBuilder(username).reverse();
        return password.contains(reversedUsername);
    }

    private static boolean isSubstringWithMoreThanTwoConsecutiveChars(String password, String substring) {
        for (int i = 0; i <= password.length() - 3; i++) {
            if (password.charAt(i) == substring.charAt(0) && password.substring(i, i + 3).equals(substring)) {
                return true;
            }
        }
        return false;
    }
}
