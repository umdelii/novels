package com.example.novel.util;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.novel.member.utils.JWTUtil;

@SpringBootTest
public class JWTUtilTest {
    private JWTUtil jwtUtil;

    @BeforeEach // @Test 등 각 메소드 실행 전 먼저 실행해야될 메소드 선언
    public void testBefore() {
        System.out.println("--------------------JWT test--------------------");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() {
        String email = "user11@gmail.com";
        String str = JWTUtil.generateToken(Map.of("email", email, "name", "user1"), 10);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws InterruptedException {
        String email = "user11@gmail.com";
        String token = JWTUtil.generateToken(Map.of("email", email, "name", "user1"), 1);

        Thread.sleep(5000); // 5초 대기

        Map<String, Object> claimMap = JWTUtil.validateToken(token);
        System.out.println(claimMap);
    }
}
