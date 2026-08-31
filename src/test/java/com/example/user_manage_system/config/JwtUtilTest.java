package com.example.user_manage_system.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-key-for-unit-test-only-123456789");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void testGenerateAndParse() {
        String token = jwtUtil.generateToken(1L, "admin");
        assertEquals(1L, jwtUtil.getUserIdFromToken(token));
        assertEquals("admin", jwtUtil.getRoleFromToken(token));
    }
}
