package com.example.novel.member.utils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {
    // signature
    private static String key = "123456789012345678901234567890123456789012345678901234567890";

    // 토큰 생성 메소드
    public static String generateToken(Map<String, Object> valueMap, int min) {
        SecretKey key = null;
        try {
            // HS256 알고리즘 방식의 암호화 키 생성 메소드 hmacShaKeyFor
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String jwtStr = Jwts.builder()
                .subject("JWT")
                .claims(valueMap)
                .issuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발급 시간 설정
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료 시간 설정
                .signWith(key)
                .compact();

        return jwtStr;
    }

    // 토큰 검증 메소드
    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("utf-8"));
            claim = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return claim;
    }
}
