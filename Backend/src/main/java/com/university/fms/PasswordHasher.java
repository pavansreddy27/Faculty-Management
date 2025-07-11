package com.university.fms;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Admin@123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Hashed password for '" + rawPassword + "': " + encodedPassword);
    }
}