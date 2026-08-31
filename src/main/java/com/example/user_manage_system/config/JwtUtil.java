package com.example.user_manage_system.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public SecretKey getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String generateToken(Long userId,String role){
        Date now=new Date();
        Date exp=new Date(now.getTime()+expiration);
        return Jwts.builder().claim("userId",userId).claim("role",role).issuedAt(now).expiration(exp).signWith(getKey()).compact();

    }
    // 解析令牌，返回里面存的所有数据
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 从令牌里取userId，Long.class告诉它返回类型是Long
    public Long getUserIdFromToken(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    // 从令牌里取role，String.class告诉它返回类型是String
    public String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }
}
