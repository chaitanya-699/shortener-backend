package com.url.backend.util;


import java.security.SecureRandom;

public class UrlCodeGenerator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final int length = 6;

    public static String generateRandomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generateUserId(String email) {
        int hash = Math.abs(email.toLowerCase().trim().hashCode());

        // Reduce to 6 digits while minimizing collisions
        return String.valueOf(100000 + (hash % 900000));
    }

}
