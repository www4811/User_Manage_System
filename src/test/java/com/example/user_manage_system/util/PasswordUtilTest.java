package com.example.user_manage_system.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void testEncodeAndMatches() {
        String encoded = PasswordUtil.encode("123456");
        assertTrue(PasswordUtil.matches("123456", encoded));
    }

    @Test
    void testWrongPassword() {
        String encoded = PasswordUtil.encode("123456");
        assertFalse(PasswordUtil.matches("000000", encoded));
    }

    @Test
    void testEncodeDifferentSalt() {
        String encoded1 = PasswordUtil.encode("123456");
        String encoded2 = PasswordUtil.encode("123456");
        assertTrue(PasswordUtil.matches("123456", encoded1));
        assertTrue(PasswordUtil.matches("123456", encoded2));
    }
}
