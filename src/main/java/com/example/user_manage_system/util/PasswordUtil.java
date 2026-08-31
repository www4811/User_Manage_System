package com.example.user_manage_system.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
