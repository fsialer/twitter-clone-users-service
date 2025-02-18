package com.fernando.ms.users.app.infrastructure.utils;

public interface PasswordUtils {
    String generateSalt();
    String hashPassword(String password, String salt);
    boolean validatePassword(String password, String salt, String hash);
}
