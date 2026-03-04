package com.example.novel.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.novel.member.dto.RegisterDTO;
import com.example.novel.member.service.NovelMemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Log4j2
public class MemberController {
    private final NovelMemberService novelMemberService;

    @PostMapping("/register")
    public ResponseEntity<String> postMethodName(@RequestBody RegisterDTO registerDTO) {
        log.info("회원가입 요청 : {}", registerDTO);

        novelMemberService.register(registerDTO);

        return ResponseEntity.ok().body("회원가입 성공");
    }

}
